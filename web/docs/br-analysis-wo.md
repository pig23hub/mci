# BR Analysis: Cộng 2 số nguyên dương từ 2 chuỗi giá trị

## 1. Mô tả yêu cầu thô

Yêu cầu được phát biểu ở dạng thô như sau:

> Tạo tính năng cộng 2 số nguyên dương với tham số đầu vào là 2 chuỗi giá trị. Với mỗi bước sẽ có log lại làm nhật kí.

Từ yêu cầu này, có thể suy ra một tính năng rất đơn giản về mặt nghiệp vụ nhưng cần làm rõ thêm về ràng buộc dữ liệu, cách hiển thị, và mức độ lưu nhật ký. Đây là dạng bài toán cộng số lớn (big integer) hoặc cộng số nguyên dương có độ dài không giới hạn, nơi dữ liệu đầu vào là chuỗi để tránh overflow trên kiểu số nguyên chuẩn.

---

## 2. Danh sách thực thể (Entities)

| Mã thực thể | Tên thực thể | Mô tả | Thuộc tính chính | Ghi chú |
| :--- | :--- | :--- | :--- | :--- |
| ENT-01 | AdditionRequest | Yêu cầu cộng 2 số từ phía client hoặc service | `leftOperand: string`, `rightOperand: string`, `requestedAt: datetime` | Dữ liệu đầu vào từ API/UI |
| ENT-02 | CalculationStep | Một bước tính toán trong quá trình cộng | `stepId: string`, `sequence: int`, `description: string`, `leftValue: string`, `rightValue: string`, `carry: string`, `result: string`, `createdAt: datetime` | Dùng để theo dõi log từng bước |
| ENT-03 | AdditionResult | Kết quả cuối cùng của phép cộng | `result: string`, `status: enum`, `completedAt: datetime` | Kết quả trả về cho client |
| ENT-04 | OperationLog | Nhật ký hệ thống cho thao tác cộng | `logId: string`, `operationName`, `message`, `level`, `createdAt`, `correlationId` | Dùng để lưu nhật ký debug / audit |
| ENT-05 | ValidationError | Lỗi dữ liệu đầu vào | `fieldName`, `message`, `errorCode` | Dùng khi đầu vào không hợp lệ |

### Đặc điểm thực thể cần lưu ý
- `AdditionRequest` và `AdditionResult` là các khái niệm nghiệp vụ cần có ở API boundary.
- `CalculationStep` có thể là khái niệm runtime trong service, không nhất thiết phải lưu trữ dài hạn nếu chỉ cần log ra console / file.
- `OperationLog` là thực thể nhật ký, nên có thể lưu ở console, file log, hoặc hệ thống log tập trung tùy thiết kế.

---

## 3. Câu hỏi còn bỏ ngỏ (Open Questions)

| Mã | Câu hỏi | Tại sao cần làm rõ | Gợi ý xác nhận |
| :--- | :--- | :--- | :--- |
| OQ-01 | Có phải số đầu vào chỉ được là số nguyên dương không, hay có thể có dấu +, dấu -, khoảng trắng, số 0 không? | Đây ảnh hưởng trực tiếp đến validation và logic cộng | Chỉ chấp nhận số nguyên dương >= 0, bỏ khoảng trắng ở đầu/cuối |
| OQ-02 | Có cần hỗ trợ số cực lớn không giới hạn (big integer) hay chỉ số trong phạm vi kiểu long? | Ảnh hưởng đến cách triển khai thuật toán và dữ liệu | Nếu là chuỗi, thường coi là số lớn không giới hạn |
| OQ-03 | Input có được nhập dưới dạng 2 chuỗi, hoặc gửi qua JSON / form / query param? | Ảnh hưởng API contract | Nên dùng JSON body với 2 trường string |
| OQ-04 | Nhật ký từng bước có cần lưu vào DB hay chỉ log ra console / file? | Ảnh hưởng vào mô hình dữ liệu và tần suất ghi | Nên log đầy đủ ở service layer, lưu file hoặc structured log |
| OQ-05 | Có cần hiển thị từng bước cộng trên UI hay chỉ hiển thị kết quả cuối? | Phụ thuộc vào mức độ nghiệp vụ / UX | Nếu yêu cầu “với mỗi bước sẽ có log lại làm nhật kí”, không bắt buộc phải hiển thị trên UI |
| OQ-06 | Có cần xác thực dữ liệu ngay tại UI trước khi gọi API không? | Ảnh hưởng UX và chất lượng validate | Nên validate cả phía UI và phía server |
| OQ-07 | Nếu đầu vào rỗng hoặc không hợp lệ, phản hồi kiểu gì? | Ảnh hưởng API error handling | Trả về 400 Bad Request với chi tiết lỗi |
| OQ-08 | Có cần xử lý số 0, số một chữ số, số dài hàng nghìn chữ số không? | Ảnh hưởng đến thuật toán | Nên hỗ trợ tất cả số nguyên dương, tối thiểu 0 và số dài |
| OQ-09 | Có cần log correlationId / requestId để trace theo phiên không? | Ảnh hưởng traceability và audit | Nên có để dễ tìm lỗi trong production |
| OQ-10 | Có cần giới hạn độ dài chuỗi đầu vào không? | Ảnh hưởng performance và security | Nên đặt max length hợp lý, ví dụ 10.000 ký tự |

---

## 4. Phân rã theo UI

| Mục | Loại | Mô tả | Yêu cầu nghiệp vụ |
| :--- | :--- | :--- | :--- |
| UI-01 | Form input | Ô nhập số thứ nhất | Kiểu text, bắt buộc, chỉ chấp nhận chuỗi số dương |
| UI-02 | Form input | Ô nhập số thứ hai | Kiểu text, bắt buộc, chỉ chấp nhận chuỗi số dương |
| UI-03 | Nút xử lý | Nút cộng | Gọi API tính tổng |
| UI-04 | Kết quả | Trường hiển thị tổng | Hiển thị chuỗi kết quả cuối cùng |
| UI-05 | Log hiển thị | Danh sách các bước tính | Hiển thị hoặc ẩn theo thiết kế; ưu tiên hiển thị nếu cần debug |
| UI-06 | Validation | Thông báo lỗi | Nếu đầu vào rỗng / không hợp lệ: báo lỗi rõ ràng |
| UI-07 | Loading state | Trạng thái đang xử lý | Disable nút, hiển thị spinner hoặc text "Đang tính" |

### Gợi ý UI validation

| Field | Rule | Mô tả |
| :--- | :--- | :--- |
| operand1 | Required | Không được để trống |
| operand2 | Required | Không được để trống |
| operand1 | Regex: `^[0-9]+$` | Chỉ số nguyên dương không có dấu âm, không có ký tự khác |
| operand2 | Regex: `^[0-9]+$` | Tương tự |
| operand1/2 | Max length | Giới hạn độ dài hợp lý, ví dụ 10.000 ký tự |

---

## 5. Phân rã theo Data

| Mục | Loại | Mô tả | Yêu cầu |
| :--- | :--- | :--- | :--- |
| DATA-01 | Input model | 2 chuỗi số đầu vào | `leftOperand: string`, `rightOperand: string` |
| DATA-02 | Result model | Kết quả cộng | `result: string` |
| DATA-03 | Step log model | Mỗi bước tính toán | `sequence`, `description`, `carry`, `result` |
| DATA-04 | Log storage | Nhật ký thao tác | Có thể lưu ở console/file hoặc DB tùy mức độ cần audit |
| DATA-05 | Validation state | Trạng thái lỗi | `field`, `message`, `code` |

### Đề xuất schema logic

```json
{
  "leftOperand": "123456789",
  "rightOperand": "987654321",
  "steps": [
    { "sequence": 1, "description": "Bước 1: cộng chữ số cuối cùng", "carry": "0", "result": "0" },
    { "sequence": 2, "description": "Bước 2: cộng chữ số tiếp theo", "carry": "1", "result": "1" }
  ],
  "result": "1111111110"
}
```

### Ràng buộc dữ liệu nên xác nhận
- Chỉ số nguyên dương: không âm, không có dấu `+`, không có dấu `-`
- Không cho phép chuỗi rỗng hoặc null
- Kết quả cộng trả về dạng chuỗi để tránh overflow
- Nếu cần lưu log, nên lưu dưới dạng structured log với `requestId` và `timestamp`

---

## 6. Phân rã theo API

### 6.1 API Endpoint đề xuất

| Mã | Method | Endpoint | Mô tả |
| :--- | :--- | :--- | :--- |
| API-01 | POST | `/api/v1/addition` | Cộng 2 chuỗi số nguyên dương |

### 6.2 Request Body

```json
{
  "leftOperand": "123",
  "rightOperand": "456"
}
```

### 6.3 Response thành công

```json
{
  "result": "579",
  "steps": [
    { "sequence": 1, "description": "Cộng 3 + 6 = 9", "carry": 0, "result": "9" },
    { "sequence": 2, "description": "Cộng 2 + 5 + 0 = 7", "carry": 0, "result": "7" },
    { "sequence": 3, "description": "Cộng 1 + 4 = 5", "carry": 0, "result": "5" }
  ],
  "requestId": "req-20260925-001"
}
```

### 6.4 Response lỗi

```json
{
  "errorCode": "INVALID_INPUT",
  "message": "leftOperand và rightOperand phải là chuỗi số nguyên dương hợp lệ"
}
```

### 6.5 Status code đề xuất

| Status | Mô tả |
| :--- | :--- |
| 200 OK | Tính toán thành công |
| 400 Bad Request | Dữ liệu đầu vào không hợp lệ |
| 500 Internal Server Error | Lỗi hệ thống / xử lý không mong đợi |

---

## 7. Mô hình xử lý nghiệp vụ đề xuất

1. Nhận request với 2 chuỗi đầu vào.
2. Validate dữ liệu:
   - không rỗng
   - chỉ chứa chữ số
   - là số nguyên dương
3. Nếu validate không đạt, trả lỗi 400.
4. Thực hiện phép cộng theo thuật toán cộng số lớn (từ phải sang trái, cộng từng cặp chữ số + nhớ).
5. Với mỗi bước, ghi log mô tả action + carry + kết quả tạm thời.
6. Trả về kết quả cuối cùng và danh sách log bước nếu cần hiển thị.
7. Ghi nhật ký hệ thống với requestId, timestamp, và mức log phù hợp.

---

## 8. Kiến nghị xác nhận trước khi triển khai

| Mã | Kiến nghị |
| :--- | :--- |
| DEC-01 | Xác nhận đầu vào chỉ là số nguyên dương, không chấp nhận số âm |
| DEC-02 | Xác nhận log từng bước chỉ cần lưu ở log system hay cần hiển thị trên UI |
| DEC-03 | Xác nhận API trả về `result` + `steps` hay chỉ `result` |
| DEC-04 | Xác nhận có cần giới hạn độ dài đầu vào hoặc không |
| DEC-05 | Xác nhận báo lỗi theo chuẩn 400 Bad Request với message rõ ràng |

---

## 9. Kết luận

Yêu cầu thô có thể được hiểu như một chức năng cộng hai số nguyên dương dạng chuỗi, ưu tiên đảm bảo không bị overflow và có khả năng theo dõi từng bước xử lý thông qua log. Về mặt phân tích BR, điều cần làm rõ trước khi xây dựng là phạm vi validation, định dạng trả về, và mức độ lưu nhật ký. Nếu xác nhận các câu hỏi còn bỏ ngỏ, việc chuyển thành API contract, UI flow, và logic service sẽ trở nên rõ ràng và dễ triển khai.
