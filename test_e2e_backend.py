import sys
import os

if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8")

from starlette.testclient import TestClient

# Ensure backend can be imported
sys.path.insert(0, os.path.abspath("."))

from backend.main import app
from backend.database import get_db_connection

client = TestClient(app)

def run_tests():
    print("=" * 60)
    print("ZENITH '26 END-TO-END BACKEND & API VERIFICATION SUITE")
    print("=" * 60)

    # 1. Test GET /api/events (Listing & 14 seed events)
    print("\n[TEST 1] Verifying GET /api/events...")
    res = client.get("/api/events")
    assert res.status_code == 200, f"Expected 200, got {res.status_code}"
    events = res.json()
    assert len(events) == 14, f"Expected 14 seed events, got {len(events)}"
    print(f"  ✓ Successfully fetched {len(events)} events.")
    first_ev = events[0]
    print(f"  ✓ First event: ID {first_ev['id']} - {first_ev['title']} ({first_ev['category']}), Seats: {first_ev['available_seats']}/{first_ev['capacity']}")

    # 2. Test Category Filters
    print("\n[TEST 2] Verifying Category Filtering...")
    categories = ["Technical", "Cultural", "Sports", "Gaming", "Workshop"]
    for cat in categories:
        cat_res = client.get(f"/api/events?category={cat}")
        assert cat_res.status_code == 200
        cat_events = cat_res.json()
        assert len(cat_events) > 0, f"No events found for {cat}"
        assert all(e["category"].lower() == cat.lower() for e in cat_events), f"Category mismatch in {cat}"
        print(f"  ✓ Category '{cat}': {len(cat_events)} events returned correctly.")

    # 3. Test Search Filtering (Case-insensitive)
    print("\n[TEST 3] Verifying Title Search...")
    search_res = client.get("/api/events?search=hackathon")
    assert search_res.status_code == 200
    search_events = search_res.json()
    assert len(search_events) == 1
    assert "HackZenith" in search_events[0]["title"]
    print(f"  ✓ Search 'hackathon' (case-insensitive) returned: '{search_events[0]['title']}'")

    # Combined Search + Category Filter
    comb_res = client.get("/api/events?category=Technical&search=hack")
    assert comb_res.status_code == 200
    assert len(comb_res.json()) == 1
    print("  ✓ Combined filter (category=Technical & search=hack) works.")

    # 4. Test Single Event Details GET /api/events/{event_id}
    print("\n[TEST 4] Verifying GET /api/events/1 Details...")
    det_res = client.get("/api/events/1")
    assert det_res.status_code == 200
    det = det_res.json()
    assert det["id"] == 1
    assert det["title"] == "HackZenith 24-Hour Hackathon"
    print(f"  ✓ Event details verified: {det['title']} @ {det['venue']}")

    # 404 for non-existent event
    det_404 = client.get("/api/events/9999")
    assert det_404.status_code == 404
    print("  ✓ Non-existent event returns 404 Not Found.")

    # 5. Test Registration Validations
    print("\n[TEST 5] Verifying Registration Validations (POST /api/events/1/register)...")
    # Empty fields (400)
    res_empty = client.post("/api/events/1/register", json={
        "student_name": "",
        "roll_number": "2026CS1042",
        "email": "test@college.edu"
    })
    assert res_empty.status_code == 400
    assert "Missing required field" in res_empty.json()["detail"]
    print(f"  ✓ Missing field rejected: {res_empty.json()['detail']}")

    # Invalid email without @ (400)
    res_invalid_email = client.post("/api/events/1/register", json={
        "student_name": "Test Student",
        "roll_number": "2026CS1042",
        "email": "invalidemail.college.edu"
    })
    assert res_invalid_email.status_code == 400
    assert "must contain '@'" in res_invalid_email.json()["detail"]
    print(f"  ✓ Invalid email rejected: {res_invalid_email.json()['detail']}")

    # 6. Test Successful Registration (201 Created)
    print("\n[TEST 6] Submitting Valid Registration...")
    test_email = "tester_student_01@college.edu"
    # Ensure test registration doesn't already exist
    conn = get_db_connection()
    conn.execute("DELETE FROM registrations WHERE email = ?;", (test_email,))
    conn.commit()
    conn.close()

    initial_seats = client.get("/api/events/1").json()["available_seats"]

    reg_payload = {
        "student_name": "Aarav Sharma",
        "roll_number": "2026CS1042",
        "email": test_email,
        "phone": "+91 9876543210"
    }
    reg_res = client.post("/api/events/1/register", json=reg_payload)
    assert reg_res.status_code == 201, f"Expected 201, got {reg_res.status_code}: {reg_res.text}"
    reg_data = reg_res.json()
    new_reg_id = reg_data["id"]
    print(f"  ✓ Registered successfully: ID #{new_reg_id}, Student: {reg_data['student_name']}")

    # Verify live seat count decreased by 1
    updated_seats = client.get("/api/events/1").json()["available_seats"]
    assert updated_seats == initial_seats - 1, f"Expected {initial_seats - 1}, got {updated_seats}"
    print(f"  ✓ Available seats updated: from {initial_seats} to {updated_seats}.")

    # 7. Test Duplicate Registration (409 Conflict)
    print("\n[TEST 7] Verifying Duplicate Registration Prevention...")
    dup_res = client.post("/api/events/1/register", json=reg_payload)
    assert dup_res.status_code == 409
    assert dup_res.json()["detail"] == "Already registered"
    print(f"  ✓ Duplicate registration rejected with HTTP 409: '{dup_res.json()['detail']}'")

    # 8. Test Admin Stats (GET /api/stats)
    print("\n[TEST 8] Verifying Admin Dashboard Stats (GET /api/stats)...")
    stats_res = client.get("/api/stats")
    assert stats_res.status_code == 200
    stats = stats_res.json()
    assert stats["total_events"] == 14
    assert stats["total_registrations"] >= 1
    assert stats["overall_utilization_pct"] >= 0.0
    print(f"  ✓ Total Events: {stats['total_events']}")
    print(f"  ✓ Total Registrations: {stats['total_registrations']}")
    print(f"  ✓ Total Capacity: {stats['total_capacity']}")
    print(f"  ✓ Overall Utilization: {stats['overall_utilization_pct']}%")
    print(f"  ✓ Category Breakdown: {stats['events_by_category']}")

    # 9. Test Registrations List (GET /api/registrations)
    print("\n[TEST 9] Verifying Registration Listing (GET /api/registrations)...")
    regs_res = client.get("/api/registrations")
    assert regs_res.status_code == 200
    regs = regs_res.json()
    assert any(r["id"] == new_reg_id for r in regs)
    print(f"  ✓ Listed {len(regs)} registrations. Found newly registered record #{new_reg_id}.")

    # Filter registrations by event_id
    filter_regs_res = client.get("/api/registrations?event_id=1")
    assert filter_regs_res.status_code == 200
    filter_regs = filter_regs_res.json()
    assert all(r["event_id"] == 1 for r in filter_regs)
    print(f"  ✓ Filtered registrations by event_id=1: {len(filter_regs)} records.")

    # 10. Test Cancel Registration (DELETE /api/registrations/{id})
    print("\n[TEST 10] Verifying Registration Cancellation (DELETE /api/registrations/{id})...")
    del_res = client.delete(f"/api/registrations/{new_reg_id}")
    assert del_res.status_code == 200
    print(f"  ✓ Cancelled registration #{new_reg_id} successfully.")

    # Verify seat freed back up
    restored_seats = client.get("/api/events/1").json()["available_seats"]
    assert restored_seats == initial_seats, f"Expected {initial_seats}, got {restored_seats}"
    print(f"  ✓ Seat immediately restored from {updated_seats} back to {restored_seats}.")

    # 11. Test Web Static Files Mount (frontend/index.html & frontend/admin.html)
    print("\n[TEST 11] Verifying Original Web Frontend Mounting...")
    idx_res = client.get("/")
    assert idx_res.status_code == 200
    assert "Zenith '26" in idx_res.text
    print("  ✓ Original Student Portal (index.html) mounts and responds with 200.")

    adm_res = client.get("/admin.html")
    assert adm_res.status_code == 200
    assert "Coordinator Dashboard" in adm_res.text
    print("  ✓ Original Admin Dashboard (admin.html) mounts and responds with 200.")

    print("\n" + "=" * 60)
    print("ALL 11 BACKEND & API TESTS PASSED SUCCESSFULLY! (100% PASS)")
    print("=" * 60)

if __name__ == "__main__":
    run_tests()
