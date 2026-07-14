-- 1. Seed Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

-- 2. Seed Users (passwords are BCrypt encrypted values of 'admin123')
-- admin@photoreview.com (Admin)
INSERT INTO users (id, email, password, display_name, bio, enabled)
VALUES (1, 'admin@photoreview.com', '$2a$10$09ZlIODKCxq/p/Az120Bcu82NUBV.dWUFuX9v5vOKVwbR5EsxzjF6', 'System Administrator', 'Platform moderator and system administrator.', TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);

-- jane@photoreview.com (Photographer/Creator)
INSERT INTO users (id, email, password, display_name, bio, enabled)
VALUES (2, 'jane@photoreview.com', '$2a$10$09ZlIODKCxq/p/Az120Bcu82NUBV.dWUFuX9v5vOKVwbR5EsxzjF6', 'Jane Doe', 'Professional landscape and portrait photographer. Passionate about natural light and capturing micro-expressions.', TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- user@photoreview.com (Regular Reviewer)
INSERT INTO users (id, email, password, display_name, bio, enabled)
VALUES (3, 'user@photoreview.com', '$2a$10$09ZlIODKCxq/p/Az120Bcu82NUBV.dWUFuX9v5vOKVwbR5EsxzjF6', 'Alex Reviewer', 'Amateur photographer and active photo critic. I appreciate sharp details and creative color grading.', TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);

-- 3. Seed Categories
INSERT INTO categories (id, name) VALUES (1, 'Portrait');
INSERT INTO categories (id, name) VALUES (2, 'Landscape');
INSERT INTO categories (id, name) VALUES (3, 'Street');
INSERT INTO categories (id, name) VALUES (4, 'Nature');
INSERT INTO categories (id, name) VALUES (5, 'Wildlife');
INSERT INTO categories (id, name) VALUES (6, 'Architecture');

-- 4. Seed Photos (Uploaded by Jane Doe)
INSERT INTO photos (id, title, description, image_url, uploader_id, category_id, uploaded_at, average_rating, total_likes)
VALUES (1, 'Serenity in the Mountains', 'Shot during sunrise in Yosemite Valley. The fog was just starting to lift, creating a mystical layer above the forest floor.', 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&q=80&w=1200', 2, 2, '2026-06-25 08:30:00', 4.50, 2);

INSERT INTO photos (id, title, description, image_url, uploader_id, category_id, uploaded_at, average_rating, total_likes)
VALUES (2, 'Golden Hour Headshot', 'Natural light portrait capture utilizing a reflector. Focus is locked on the eyes with a shallow depth of field (f/1.8).', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=1200', 2, 1, '2026-06-26 14:15:00', 5.00, 1);

INSERT INTO photos (id, title, description, image_url, uploader_id, category_id, uploaded_at, average_rating, total_likes)
VALUES (3, 'Rainy Streets of Tokyo', 'Shinjuku neon lights reflecting on wet tarmac after a sudden summer shower. Handheld at 1/60s, ISO 1600.', 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&q=80&w=1200', 2, 3, '2026-06-27 21:40:00', 4.00, 1);

-- 5. Seed Ratings (One rating per user per photo constraint)
-- Yosemite photo (Avg: 4.5)
INSERT INTO ratings (id, user_id, photo_id, score, rated_at) VALUES (1, 3, 1, 5, '2026-06-25 09:00:00');
INSERT INTO ratings (id, user_id, photo_id, score, rated_at) VALUES (2, 1, 1, 4, '2026-06-25 10:15:00');

-- Portrait photo (Avg: 5.0)
INSERT INTO ratings (id, user_id, photo_id, score, rated_at) VALUES (3, 3, 2, 5, '2026-06-26 15:30:00');

-- Tokyo photo (Avg: 4.0)
INSERT INTO ratings (id, user_id, photo_id, score, rated_at) VALUES (4, 3, 3, 4, '2026-06-27 22:00:00');

-- 6. Seed Comments
-- Yosemite Photo
INSERT INTO comments (id, user_id, photo_id, content, created_at)
VALUES (1, 3, 1, 'The composition here is stunning. Love the layers of fog and the warm light hitting the mountain peaks. Excellent work!', '2026-06-25 09:05:00');
INSERT INTO comments (id, user_id, photo_id, content, created_at)
VALUES (2, 1, 1, 'Maybe crop slightly from the left to lead the eyes more towards the reflection. Still a very beautiful capture.', '2026-06-25 10:20:00');

-- Portrait Photo
INSERT INTO comments (id, user_id, photo_id, content, created_at)
VALUES (3, 3, 2, 'Stunning bokeh and gorgeous lighting. The focus is exceptionally sharp on the left eye. What lens did you use?', '2026-06-26 15:32:00');

-- Tokyo Photo
INSERT INTO comments (id, user_id, photo_id, content, created_at)
VALUES (4, 1, 3, 'Great mood! Shutter speed was a bit slow, resulting in slight motion blur on the pedestrians. But the neon reflections make up for it.', '2026-06-27 22:05:00');

-- 7. Seed Likes
-- Yosemite
INSERT INTO likes (id, user_id, photo_id, liked_at) VALUES (1, 3, 1, '2026-06-25 09:02:00');
INSERT INTO likes (id, user_id, photo_id, liked_at) VALUES (2, 1, 1, '2026-06-25 10:16:00');

-- Portrait
INSERT INTO likes (id, user_id, photo_id, liked_at) VALUES (3, 3, 2, '2026-06-26 15:31:00');

-- Tokyo
INSERT INTO likes (id, user_id, photo_id, liked_at) VALUES (4, 3, 3, '2026-06-27 22:01:00');
