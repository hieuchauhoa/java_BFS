# Java BFS Graph Search — Client/Server

Ứng dụng Java (Maven) minh hoạ thuật toán tìm kiếm theo chiều rộng (Breadth-First Search) trên đồ thị, theo mô hình client/server.

## Cấu trúc

- `common/Graph.java`, `common/FileInfo.java` — cấu trúc đồ thị dùng chung.
- `server/server.java` — server xử lý truy vấn BFS.
- `client/client.java` — client gửi yêu cầu tới server.

## Chạy thử

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.mycompany.breadthfirstpaths.server.server"
# ở terminal khác
mvn exec:java -Dexec.mainClass="com.mycompany.breadthfirstpaths.client.client"
```

## Tác giả

Minh Hiếu Calan Tog.
