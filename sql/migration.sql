-- Migration from the old local-storage schema (image_data BLOB)
-- to the new Cloudinary URL schema (image_url VARCHAR).
--
-- WARNING: Existing binary image data cannot be migrated automatically
-- to Cloudinary. Re-upload those photos through the admin page after
-- running this script.

USE photo_gallery;

ALTER TABLE photos
    ADD COLUMN image_url VARCHAR(500) NULL AFTER mime_type;

-- If you are starting fresh and have no photos to keep:
-- TRUNCATE TABLE photos;

ALTER TABLE photos
    DROP COLUMN image_data;

ALTER TABLE photos
    MODIFY image_url VARCHAR(500) NOT NULL;
