# Business Requirements Analysis - Work Order Management

## 1. Danh sách các thực thể (Entities) và thuộc tính cơ bản

### 1.1 WorkOrder
Là phiếu công việc do thợ kỹ thuật tạo ra trên ứng dụng di động.

Thuộc tính cơ bản:
- workOrderId: mã định danh duy nhất của phiếu công việc
- equipmentId: mã thiết bị đang được báo hỏng hoặc cần xử lý
- priority: mức độ ưu tiên (`low`, `medium`, `high`, `urgent`)
- description: mô tả công việc cần thực hiện
- createdBy: người tạo phiếu (thợ kỹ thuật)
- createdAt: thời điểm tạo phiếu
- status: trạng thái phiếu (ví dụ: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
- assignedTo: người được giao xử lý (nếu có phân công sau này)
- assignedAt: thời điểm giao việc
- resolvedAt: thời điểm hoàn thành xử lý
- notes: ghi chú bổ sung của quản đốc hoặc thợ (nếu cần mở rộng sau này)

### 1.2 Equipment
Là thiết bị đang hoạt động trong công trường.

Thuộc tính cơ bản:
- equipmentId: mã thiết bị duy nhất
- equipmentName: tên thiết bị
- status: trạng thái hoạt động (`ACTIVE`, `INACTIVE`, `MAINTENANCE`)
- location: vị trí lắp đặt / công trường
- lastMaintenanceAt: thời gian bảo dưỡng gần nhất

### 1.3 Technician
Là người dùng trên ứng dụng công trường.

Thuộc tính cơ bản:
- technicianId: mã nhân viên/thợ
- fullName: tên đầy đủ
- username: tên đăng nhập
- role: vai trò người dùng (ví dụ: `TECHNICIAN`, `SUPERVISOR`)
- siteId: mã công trường / địa điểm làm việc

### 1.4 Supervisor / Manager
Là người xem danh sách và giám sát công việc.

Thuộc tính cơ bản:
- supervisorId: mã quản đốc
- fullName: tên quản đốc
- siteId: mã công trường phụ trách

### 1.5 WorkOrderHistory (nếu cần mở rộng sau này)
Dùng để lưu lịch sử thay đổi trạng thái hoặc ghi chú.

Thuộc tính cơ bản:
- historyId: mã lịch sử
- workOrderId: phiếu liên quan
- changedBy: người thay đổi
- changeType: loại thay đổi (`STATUS`, `ASSIGNMENT`, `NOTE`)
- changedAt: thời điểm thay đổi
- oldValue: giá trị cũ
- newValue: giá trị mới

---

## 2. Các câu hỏi còn bỏ ngỏ / Rủi ro nghiệp vụ cần Product Owner làm rõ (Open Questions)

### 2.1 Xác định người dùng và phân quyền
- Ai là người được phép tạo Work Order? Chỉ thợ kỹ thuật hay cả quản đốc, kỹ sư, bảo trì cũng được tạo?
- Nếu app công trường cho phép mọi người đăng nhập, liệu cần giới hạn theo vai trò hoặc theo site không?
- Có cần phân quyền rõ ràng giữa `Technician` và `Supervisor` không?

### 2.2 Kiểm tra trạng thái thiết bị
- Mã thiết bị `equipment_id` phải đảm bảo thiết bị đang hoạt động trước khi tạo phiếu hay không?
- Nếu mã thiết bị không tồn tại hoặc đã ngừng hoạt động, hệ thống nên từ chối tạo phiếu bằng cách nào?
- Có cần kiểm tra trạng thái `ACTIVE` ngay trên app hay chỉ ở backend?

### 2.3 Quy tắc ưu tiên
- `priority` có dùng enum cố định hay cần cho phép nhập tùy biến?
- Có quy tắc nghiệp vụ nào: `urgent` phải được xử lý trong vòng X giờ, `high` trong vòng Y giờ, v.v.?
- Có cần cảnh báo khi ưu tiên quá cao nhưng chưa có người xử lý không?

### 2.4 Nội dung mô tả công việc
- `description` có bắt buộc nhập không? Nếu có, giới hạn ký tự tối đa là bao nhiêu?
- Có cần định dạng chuẩn (ví dụ: mô tả bước, lỗi, ảnh đính kèm) không?
- Có bắt buộc đính kèm hình ảnh hoặc video không?

### 2.5 Trạng thái và luồng làm việc
- Phiếu công việc nên bắt đầu ở trạng thái nào? `OPEN` hay `NEW`?
- Sau khi thợ tạo ra, quản đốc có được chỉnh sửa trạng thái không?
- Khi công việc hoàn tất, thợ phải cập nhật ngay trạng thái hay quản đốc mới đóng phiếu?

### 2.6 Phạm vi nhìn thấy dữ liệu
- Quản đốc ở công trường xem được toàn bộ phiếu của site mình hay chỉ những phiếu do chính site đó tạo?
- Có cần lọc theo ngày, ưu tiên, trạng thái, thiết bị không?
- Có cần khung tìm kiếm theo `equipmentId`, `status`, `createdBy` không?

### 2.7 Dữ liệu lưu trữ và lịch sử
- Phiếu công việc có cần lưu toàn bộ lịch sử thay đổi không?
- Có cần audit log theo thời gian và người thực hiện không?
- Có cần đồng bộ offline cho ứng dụng di động khi không có mạng không?

### 2.8 Điều kiện thời gian / SLA
- Có yêu cầu SLA theo mức ưu tiên không?
- Có cần tính toán thời gian phản hồi hay thời gian xử lý trung bình không?

### 2.9 Tính khả dụng / làm nhanh trong tuần này
- Vì là tính năng gấp, nên scope ưu tiên là gì: tạo phiếu + xem danh sách + lọc cơ bản, hay phải có full workflow cập nhật trạng thái?
- Có cần tối giản để hoàn thành nhanh trong tuần này mà không làm phức tạp các phân quyền hay workflow dài?

### 2.10 Xử lý lỗi / validation
- Nếu `equipmentId` không tồn tại, hệ thống nên trả về lỗi nào?
- Nếu `description` rỗng hoặc quá dài, nên reject hoặc lưu như default text?
- Nếu `priority` không hợp lệ, hệ thống có nên reject bằng validation rõ ràng không?

---

## 3. Bảng phân rã chi tiết theo mô hình 3 tầng (UI / Data / API)

### 3.1 Tầng UI (Mobile App)

| Mục | Chi tiết | Ghi chú |
|---|---|---|
| Screen | Tạo Work Order | Màn hình nhập phiếu công việc |
| Input fields | `equipmentId`, `priority`, `description` | Bắt buộc hoặc tùy validation |
| Validation | Kiểm tra `equipmentId` không rỗng, `priority` hợp lệ, `description` không rỗng | Theo nghiệp vụ xác định |
| Submit action | Gửi dữ liệu lên backend | Nút tạo phiếu |
| List screen | Danh sách Work Order | Hiển thị cho quản đốc hoặc thợ |
| Filter | Theo trạng thái, ưu tiên, thiết bị, ngày | Có thể cần trong phiên bản 1 |
| Error handling | Hiển thị lỗi validation hoặc lỗi backend rõ ràng | Không lộ PII hoặc nhạy cảm |
| Success state | Hiển thị thông báo tạo phiếu thành công | Ví dụ: “Phiếu công việc đã được lưu” |

#### UI flow đề xuất
1. Thợ mở màn hình tạo Work Order.
2. Nhập `equipmentId`.
3. Chọn `priority`.
4. Nhập `description`.
5. Nút `Submit`.
6. Hệ thống validate và lưu.
7. Hiển thị tin nhắn thành công hoặc lỗi.
8. Quản đốc xem danh sách phiếu trong màn hình dashboard/list.

### 3.2 Tầng Data

| Mục | Chi tiết | Ghi chú |
|---|---|---|
| Data entity | `WorkOrder` | Bản ghi chính |
| Related entity | `Equipment` | Kiểm tra thiết bị có hoạt động không |
| Related entity | `Technician` | Người tạo phiếu |
| Related entity | `Supervisor` | Người xem và quản lý |
| Status model | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` | Có thể ví dụ ban đầu |
| Priority model | `low`, `medium`, `high`, `urgent` | Enum cố định |
| Storage | Database table / persistence layer | Dùng JPA / repository nếu Spring Boot |
| Validation rules | `equipmentId` tồn tại, `priority` hợp lệ, `description` không trống | Ràng buộc nghiệp vụ |

#### Data design gợi ý
- `work_order` table:
  - work_order_id
  - equipment_id
  - priority
  - description
  - created_by
  - created_at
  - status
  - assigned_to
  - assigned_at
  - resolved_at

- `equipment` table:
  - equipment_id
  - name
  - status
  - location

- `user` / `technician` table:
  - user_id
  - full_name
  - role
  - site_id

### 3.3 Tầng API

| Mục | Chi tiết | Ghi chú |
|---|---|---|
| Resource | `/api/workorders` | Danh sách resource theo chuẩn REST |
| Method | `POST` | Tạo phiếu mới |
| Request body | `equipmentId`, `priority`, `description` | Theo contract |
| Validation | `@Valid`, `@NotBlank`, `@NotNull` | Theo quy tắc API |
| Response success | `201 Created` | Trả về WorkOrder vừa tạo |
| Response error | `400 Bad Request` | Validation lỗi |
| Error format | RFC 7807 Problem Details | `type`, `title`, `status`, `detail` |
| Read API | `GET /api/workorders` | Lấy danh sách phiếu |
| Optional filter | `?status=OPEN&priority=urgent` | Nếu cần trong version 1 |
| Optional detail API | `GET /api/workorders/{id}` | Xem chi tiết phiếu |

#### Ví dụ request payload
```json
{
  "equipmentId": "EQ-001",
  "priority": "high",
  "description": "Máy bơm bị nóng và không khởi động"
}
```

#### Ví dụ response success
```json
{
  "workOrderId": "WO-1001",
  "equipmentId": "EQ-001",
  "priority": "high",
  "description": "Máy bơm bị nóng và không khởi động",
  "status": "OPEN",
  "createdBy": "TCH-01",
  "createdAt": "2026-09-22T10:15:00Z"
}
```

#### Ví dụ response lỗi
```json
{
  "type": "https://api.posco-mci.local/problems/validation-error",
  "title": "Validation failed",
  "status": 400,
  "detail": "equipmentId is required and priority must be one of low, medium, high, urgent.",
  "instance": "/api/workorders"
}
```

---

## 4. Kết luận

Yêu cầu hiện tại cho thấy đây là tính năng gấp, ưu tiên tối thiểu nhưng rõ nghiệp vụ: thợ kỹ thuật tạo phiếu công việc với thiết bị, mức ưu tiên và mô tả, quản đốc xem danh sách. Tuy nhiên, để triển khai đúng và an toàn, cần xác định thêm các câu hỏi về validation, phân quyền, trạng thái, SLA, và phạm vi dữ liệu. Theo chuẩn yêu cầu, cần thỏa mãn các nguyên tắc API, security và coding rule đã định nghĩa trong project.
