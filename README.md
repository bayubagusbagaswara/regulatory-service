# Regulatory Service

# Upload File
- Pada Billing kemarin, jika column tidak diisi (datanya kosong), maka akan dikirim `NULL` ke belakang
- Artinya jika melakukan proses `UPDATE`, maka sebenarnya kolom tersebut tidak ingin diupdate
- Jadi kita harus perhatikan handling tersebut