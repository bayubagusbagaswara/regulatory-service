# Regulatory Service

# Upload File
- Pada Billing kemarin, jika column tidak diisi (datanya kosong), maka akan dikirim `NULL` ke belakang
- Artinya jika melakukan proses `UPDATE`, maka sebenarnya kolom tersebut tidak ingin diupdate
- Jadi kita harus perhatikan handling tersebut

# Set Cross Origin di properties
# application.properties
# Mengizinkan semua asal
spring.web.mvc.cors.allowed-origins=*
# Mengizinkan semua header
spring.web.mvc.cors.allowed-headers=*
# Mengizinkan semua metode HTTP
spring.web.mvc.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS