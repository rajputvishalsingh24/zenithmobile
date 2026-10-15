"""
Database Migration Utility: SQLite -> PostgreSQL for Zenith '26
Usage:
    python -m backend.migrate_to_postgres <POSTGRES_DATABASE_URL>

Or set DATABASE_URL environment variable and run:
    python -m backend.migrate_to_postgres
"""

import os
import sqlite3
import sys
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
if str(BASE_DIR) not in sys.path:
    sys.path.insert(0, str(BASE_DIR))

import psycopg2
import psycopg2.extras

DB_PATH = BASE_DIR / "fest.db"


def migrate(target_pg_url: str):
    print("=" * 60)
    print("ZENITH '26: MIGRATING SQLITE (fest.db) -> POSTGRESQL")
    print("=" * 60)

    if not DB_PATH.exists():
        print(f"Error: SQLite database '{DB_PATH}' not found!")
        sys.exit(1)

    # Clean URL format
    if target_pg_url.startswith("postgres://"):
        target_pg_url = target_pg_url.replace("postgres://", "postgresql://", 1)

    print(f"Connecting to PostgreSQL: {target_pg_url.split('@')[-1] if '@' in target_pg_url else 'PostgreSQL'}...")
    pg_conn = psycopg2.connect(target_pg_url)
    pg_cur = pg_conn.cursor()

    # 1. Create tables in PostgreSQL
    print("\n1. Initializing PostgreSQL tables (events, registrations)...")
    pg_cur.execute(
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
    pg_cur.execute(
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
    pg_conn.commit()
    print("  ✓ Tables created successfully.")

    # 2. Read from SQLite
    print("\n2. Reading events and registrations from SQLite...")
    sqlite_conn = sqlite3.connect(DB_PATH)
    sqlite_conn.row_factory = sqlite3.Row
    events = sqlite_conn.execute("SELECT * FROM events ORDER BY id ASC;").fetchall()
    registrations = sqlite_conn.execute("SELECT * FROM registrations ORDER BY id ASC;").fetchall()
    print(f"  Found {len(events)} events and {len(registrations)} registrations in SQLite.")

    # 3. Migrate Events
    print("\n3. Migrating events into PostgreSQL...")
    events_migrated = 0
    for ev in events:
        pg_cur.execute(
            """
            INSERT INTO events (id, title, category, description, event_date, event_time, venue, capacity, fee, icon)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (id) DO UPDATE SET
                title = EXCLUDED.title,
                category = EXCLUDED.category,
                description = EXCLUDED.description,
                event_date = EXCLUDED.event_date,
                event_time = EXCLUDED.event_time,
                venue = EXCLUDED.venue,
                capacity = EXCLUDED.capacity,
                fee = EXCLUDED.fee,
                icon = EXCLUDED.icon;
            """,
            (
                ev["id"],
                ev["title"],
                ev["category"],
                ev["description"],
                ev["event_date"],
                ev["event_time"],
                ev["venue"],
                ev["capacity"],
                ev["fee"],
                ev["icon"],
            ),
        )
        events_migrated += 1
    pg_conn.commit()

    # Reset events sequence
    pg_cur.execute("SELECT setval(pg_get_serial_sequence('events', 'id'), COALESCE(MAX(id), 1)) FROM events;")
    pg_conn.commit()
    print(f"  ✓ Migrated {events_migrated} events. Sequence reset.")

    # 4. Migrate Registrations
    print("\n4. Migrating registrations into PostgreSQL...")
    regs_migrated = 0
    for reg in registrations:
        pg_cur.execute(
            """
            INSERT INTO registrations (id, event_id, student_name, roll_number, email, phone, registered_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (event_id, email) DO NOTHING;
            """,
            (
                reg["id"],
                reg["event_id"],
                reg["student_name"],
                reg["roll_number"],
                reg["email"],
                reg["phone"],
                reg["registered_at"],
            ),
        )
        regs_migrated += 1
    pg_conn.commit()

    # Reset registrations sequence
    pg_cur.execute("SELECT setval(pg_get_serial_sequence('registrations', 'id'), COALESCE(MAX(id), 1)) FROM registrations;")
    pg_conn.commit()
    print(f"  ✓ Migrated {regs_migrated} registrations. Sequence reset.")

    sqlite_conn.close()
    pg_conn.close()
    print("\n" + "=" * 60)
    print("MIGRATION TO POSTGRESQL COMPLETED SUCCESSFULLY!")
    print("=" * 60)


if __name__ == "__main__":
    url = sys.argv[1] if len(sys.argv) > 1 else os.getenv("DATABASE_URL")
    if not url:
        print("Usage:")
        print("  python -m backend.migrate_to_postgres <POSTGRES_DATABASE_URL>")
        print("  Or set DATABASE_URL environment variable first.")
        sys.exit(1)

    migrate(url)
