ALTER TABLE employee
    ADD COLUMN avatar_url VARCHAR(1024) NULL, # là URL để FE hiển thị.
    ADD COLUMN avatar_public_id VARCHAR(255) NULL; # là khóa để Cloudinary xoá file cũ.