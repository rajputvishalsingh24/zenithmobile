import os
import sys
import traceback
from pathlib import Path

# Add project root to sys.path so 'backend' can be resolved
ROOT_DIR = Path(__file__).resolve().parent.parent
if str(ROOT_DIR) not in sys.path:
    sys.path.insert(0, str(ROOT_DIR))

try:
    from backend.main import app
    handler = app
except Exception as e:
    from fastapi import FastAPI
    from fastapi.responses import JSONResponse

    app = FastAPI(title="Zenith '26 - Startup Diagnostic")
    handler = app
    err_traceback = traceback.format_exc()

    @app.api_route("/{path_name:path}", methods=["GET", "POST", "PUT", "DELETE"])
    @app.api_route("/", methods=["GET", "POST", "PUT", "DELETE"])
    async def diagnostic_fallback(path_name: str = ""):
        return JSONResponse(
            status_code=500,
            content={
                "error": "FastAPI failed to start on Vercel",
                "detail": str(e),
                "traceback": err_traceback,
                "hint": "Check Vercel Environment Variables (DATABASE_URL) and Python packages in requirements.txt.",
            },
        )
