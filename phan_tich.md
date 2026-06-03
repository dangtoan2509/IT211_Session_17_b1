
## Phân tích lỗi
- Lý do hệ thống thông báo lỗi Invalid JWT là do bất đối xứng về Secret Key giữa quá trình Ký (Sign) và quá trình Xác minh.

- Cơ chế hoạt động của thuật toán đối xứng (HS256):
  - Khi tạo Token: Hệ thống dùng key (Key thứ nhất) kết hợp với thuật toán HS256 để băm (hash) phần Header và Payload, tạo ra chữ ký số (Signature) đính kèm ở cuối Token.

  - Khi xác minh Token: Hệ thống bắt buộc phải dùng chính xác key ban đầu đó để tính toán lại chữ ký từ Header và Payload nhận được, sau đó so sánh với chữ ký đính kèm trong Token.

- Nguyên nhân lỗi trong đoạn code:

```java
    Key differentKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
```

- Hàm Keys.secretKeyFor() sẽ tạo ngẫu nhiên một Key hoàn toàn mới mỗi khi nó được gọi. Do đó, differentKey hoàn toàn khác với key. Khi đưa differentKey vào setSigningKey(), bộ parser của JJWT phát hiện ra chữ ký được tính lại không khớp với chữ ký có sẵn trên Token và lập tức từ chối "vé điện tử" này.