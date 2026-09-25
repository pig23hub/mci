<!-- Instruction for Copilot Chat / Prompts:
"Generate a Markdown technical design document for docs/work-order-decomposition.md based on Issue WO-201. Include UI validation tables, PostgreSQL DDL schema, and REST API payload contracts."
-->

# Technical Specification: Work Order Domain (WO-201)

## 1. UI Validation Matrix
| Field Name | Type | Required | Rules / Constraints |
| :--- | :--- | :--- | :--- |
| `title` | String | Yes | Min 5 chars, Max 255 chars |
| `description` | String | No | Max 2000 chars |
| `priority` | Enum | Yes | Default: `MED` (`LOW`, `MED`, `HIGH`, `CRITICAL`) |
| `customer_id` | UUID | Yes | Must reference valid Active Customer |

## 2. Data Schema (PostgreSQL)
```sql
CREATE TABLE work_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL DEFAULT 'MED',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    customer_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_priority CHECK (priority IN ('LOW', 'MED', 'HIGH', 'CRITICAL'))
);
```

## 3. REST API Contract
**Request Header:** `Authorization: Bearer <token>`  
**POST** `/api/v1/work-orders`

**Request Body:**
```json
{
  "title": "HVAC Repair Unit 4",
  "description": "System reporting error code E-42",
  "priority": "HIGH",
  "customer_id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
}
```

**Response Body (HTTP 201 Created):**
```json
{
  "id": "c9bf9e57-1685-4c89-bafb-ff5af830be8a",
  "status": "DRAFT",
  "created_at": "2026-08-30T10:00:00Z"
}
```
