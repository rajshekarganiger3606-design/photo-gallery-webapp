-- Quick fix for: Unknown column 'image_url' in 'field list'
-- Run this in MySQL Workbench or: mysql -u root -p photo_gallery < sql/fix-image-url.sql

USE photo_gallery;

-- Remove old rows (they used local BLOB storage and cannot display via Cloudinary URLs)
DELETE FROM photos;

-- Add the new column if it does not exist yet
ALTER TABLE photos
    ADD COLUMN image_url VARCHAR(500) NOT NULL DEFAULT '' AFTER mime_type;

-- Remove the old BLOB column
ALTER TABLE photos
    DROP COLUMN image_data;

-- Clean up the temporary default
ALTER TABLE photos
    MODIFY image_url VARCHAR(500) NOT NULL;
