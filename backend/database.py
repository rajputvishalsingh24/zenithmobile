import os
import sqlite3
from pathlib import Path
from typing import Any, List, Optional, Tuple, Union

try:
    import psycopg2
    import psycopg2.extras
    from psycopg2 import IntegrityError as PgIntegrityError
    HAS_PSYCOPG2 = True
except ImportError:
    HAS_PSYCOPG2 = False
    PgIntegrityError = None

import tempfile

BASE_DIR = Path(__file__).resolve().parent.parent

# In serverless environments (Vercel, AWS Lambda), /var/task is read-only.
# tempfile.gettempdir() (/tmp on Linux) is the only writable directory for temporary SQLite storage.
IS_SERVERLESS = bool(os.getenv("VERCEL") or os.getenv("AWS_LAMBDA_FUNCTION_NAME"))
if IS_SERVERLESS:
    DB_PATH = Path(tempfile.gettempdir()) / "fest.db"
else:
    DB_PATH = BASE_DIR / "fest.db"

# Unified IntegrityError tuple so `except IntegrityError:` catches both
if PgIntegrityError:
    IntegrityError = (sqlite3.IntegrityError, PgIntegrityError)
else:
    IntegrityError = (sqlite3.IntegrityError,)


def get_database_url() -> Optional[str]:
    """Retrieve database URL from environment variables, checking common Vercel/Neon/Supabase keys."""
    for key in ("DATABASE_URL", "POSTGRES_URL", "POSTGRES_PRISMA_URL", "POSTGRES_URL_NON_POOLING"):
        val = os.getenv(key)
        if val and val.strip():
            url = val.strip()
            if url.startswith("postgres://"):
                url = url.replace("postgres://", "postgresql://", 1)
            # Ensure SSL mode for remote cloud PostgreSQL connections
            if "@localhost" not in url and "@127.0.0.1" not in url and "sslmode=" not in url:
                separator = "&" if "?" in url else "?"
                url = f"{url}{separator}sslmode=require"
            return url
    return None


def is_postgres() -> bool:
    """Return True if a PostgreSQL connection URL is configured in the environment."""
    return get_database_url() is not None


class PostgresCursorWrapper:
    """Cursor wrapper for psycopg2 that provides sqlite3-compatible method signatures."""

    def __init__(self, raw_cursor):
        self._cursor = raw_cursor
        self.lastrowid = None

    def execute(self, sql: str, params: Optional[Union[Tuple, List]] = None):
        adapted_sql = sql.replace("?", "%s")
        is_insert = adapted_sql.strip().upper().startswith("INSERT")
        needs_returning = is_insert and "RETURNING" not in adapted_sql.upper()

        if needs_returning:
            cleaned = adapted_sql.rstrip().rstrip(";")
            adapted_sql = f"{cleaned} RETURNING id;"

        if params is not None:
            self._cursor.execute(adapted_sql, params)
        else:
            self._cursor.execute(adapted_sql)

        if needs_returning:
            try:
                row = self._cursor.fetchone()
                if row:
                    self.lastrowid = row[0]
            except Exception:
                self.lastrowid = None

        return self

    def fetchone(self):
        return self._cursor.fetchone()

    def fetchall(self):
        return self._cursor.fetchall()

    def fetchmany(self, size=None):
        return self._cursor.fetchmany(size) if size else self._cursor.fetchmany()

    def close(self):
        self._cursor.close()

    @property
    def rowcount(self):
        return self._cursor.rowcount

    def __iter__(self):
        return iter(self._cursor)


class PostgresConnectionWrapper:
    """Connection wrapper for psycopg2 that provides sqlite3-compatible execute & context methods."""

    def __init__(self, raw_conn):
        self._conn = raw_conn

    def cursor(self):
        return PostgresCursorWrapper(self._conn.cursor())

    def execute(self, sql: str, params: Optional[Union[Tuple, List]] = None):
        cur = self.cursor()
        cur.execute(sql, params)
        return cur

    def commit(self):
        self._conn.commit()

    def rollback(self):
        self._conn.rollback()

    def close(self):
        self._conn.close()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if exc_type:
            self.rollback()
        else:
            self.commit()
        self.close()


def _create_raw_connection():
    """Create a raw database connection without triggering ensure_initialized recursion."""
    db_url = get_database_url()
    if db_url:
        if not HAS_PSYCOPG2:
            raise RuntimeError("psycopg2 is required for PostgreSQL. Please install psycopg2-binary.")
        raw_conn = psycopg2.connect(db_url, cursor_factory=psycopg2.extras.DictCursor)
        return PostgresConnectionWrapper(raw_conn)

    # SQLite Fallback
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON;")
    return conn


_initialized = False
_initializing = False


def ensure_initialized() -> None:
    """Ensure database tables exist and initial seed events are populated."""
    global _initialized, _initializing
    if not _initialized and not _initializing:
        _initializing = True
        try:
            init_db()
            from backend.seed_data import seed_events
            seed_events()
            _initialized = True
        except Exception as e:
            print(f"Database initialization notice: {e}")
        finally:
            _initializing = False


def get_db_connection():
    """
    Return a database connection.
    Connects to PostgreSQL if DATABASE_URL or POSTGRES_URL is configured.
    Otherwise falls back to SQLite fest.db with Row factory.
    """
    ensure_initialized()
    return _create_raw_connection()


def init_db() -> None:
    """Create the events and registrations tables if they do not already exist."""
    conn = _create_raw_connection()
    try:
        if is_postgres():
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS events (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    category VARCHAR(100) NOT NULL,
                    description TEXT,
                    event_date VARCHAR(50) NOT NULL,
                    event_time VARCHAR(50) NOT NULL,
                    venue VARCHAR(255) NOT NULL,
                    capacity INTEGER NOT NULL,
                    fee INTEGER NOT NULL DEFAULT 0,
                    icon VARCHAR(50)
                );
                """
            )
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS registrations (
                    id SERIAL PRIMARY KEY,
                    event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
                    student_name VARCHAR(255) NOT NULL,
                    roll_number VARCHAR(100) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    phone VARCHAR(50),
                    registered_at VARCHAR(50) NOT NULL,
                    UNIQUE(event_id, email)
                );
                """
            )
            conn.commit()
        else:
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT,
                    event_date TEXT NOT NULL,
                    event_time TEXT NOT NULL,
                    venue TEXT NOT NULL,
                    capacity INTEGER NOT NULL,
                    fee INTEGER NOT NULL DEFAULT 0,
                    icon TEXT
                );
                """
            )
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS registrations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_id INTEGER NOT NULL REFERENCES events(id),
                    student_name TEXT NOT NULL,
                    roll_number TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT,
                    registered_at TEXT NOT NULL,
                    UNIQUE(event_id, email)
                );
                """
            )
            conn.commit()
    finally:
        conn.close()
