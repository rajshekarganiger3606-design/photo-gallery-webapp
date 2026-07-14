# PhotoReview – Online Public Photo Review Website

A premium, full-stack **Java Spring Boot 3.2.4** and **MySQL** web application where photographers can showcase their work and receive structured rating critiques, comments, and suggestions from the public.

---

## 🚀 Key Features

### 1. Public Visitor Experience (No Login Required)
*   **Landing Page**: Hero presentation showing the 3 latest uploads and the 3 most popular (most liked/rated) submissions.
*   **Filterable Gallery**: Organizes photos in a clean Masonry grid. Visitors can filter by category pills (Portrait, Landscape, Nature, etc.), search by keyword (matching titles, categories, or photographer names), and sort by newest uploads, highest average rating, or total likes.
*   **Photographer Portfolios**: Clicking a photographer's name opens their public profile page, calculating and presenting their overall stats (total uploaded count, total likes received, and average rating mean).
*   **Fullscreen Lightbox**: Clicking any thumbnail opens a smooth fullscreen image preview.

### 2. Registered Users (ROLE_USER)
*   **5-Star Rating Critique**: Submit star ratings (1 to 5) on photos. Database constraints enforce **one rating per user per photo**, and average ratings are recalculated instantly.
*   **Likes System**: Toggle likes on photos. Enforces **one like per user per photo**.
*   **Constructive Feedback Thread**: Post critiques and comment on photos. Users can delete their own comments if they want to revise their input.
*   **Photographer Dashboard**:
    *   *Upload Panel*: Submits new photo files, inputs titles/descriptions, and assigns category tags. File uploads are **auto-converted and compressed to JPEG** to optimize storage.
    *   *Analytics panel*: Tracks personal upload counts, total likes received, and average rating values.
    *   *Critique Feed*: Reviews recent comments posted on the photographer's uploads.
    *   *Photo Manager*: Edit details or delete uploaded photos from both database records and physical server disk storage.

### 3. Secured Administration (ROLE_ADMIN)
*   **Admin Dashboard**: Tracks core system statistics: total users count, total photos, total reviews/ratings, and comments posted.
*   **Account Moderator**: Suspension (enable/disable toggles) and account deletion controls.
*   **Photos Moderator**: Delete inappropriate photos from the global portfolio.
*   **Comments Moderator**: Remove abusive or off-topic comments.
*   **Categories CRUD**: Add or delete category tags.

---

## 📂 Project Directory Structure

```
photo-review-website/
├── pom.xml                        # Maven configuration
├── README.md                      # Setup documentation
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── photoreview/
        │           ├── PhotoReviewApplication.java  # Main launch class
        │           ├── config/
        │           │   └── SecurityConfig.java      # Security Filter Chains
        │           ├── controller/
        │           │   ├── HomeController.java      # Base pages & routing
        │           │   ├── AuthController.java      # Logins & registrations
        │           │   ├── DashboardController.java # Photographer uploads
        │           │   ├── PhotoController.java     # Details, likes, and ratings
        │           │   ├── ProfileController.java   # Settings editor
        │           │   └── AdminController.java     # Admin panels
        │           ├── service/
        │           │   ├── UserService.java
        │           │   ├── PhotoService.java
        │           │   ├── CategoryService.java
        │           │   ├── LikeService.java
        │           │   ├── RatingService.java
        │           │   ├── CommentService.java
        │           │   └── FileStorageService.java  # Image conversion & compression
        │           ├── repository/
        │           │   ├── UserRepository.java
        │           │   ├── RoleRepository.java
        │           │   ├── PhotoRepository.java
        │           │   ├── CategoryRepository.java
        │           │   ├── LikeRepository.java
        │           │   ├── RatingRepository.java
        │           │   └── CommentRepository.java
        │           ├── model/
        │           │   ├── User.java                # JPA Entities
        │           │   ├── Role.java
        │           │   ├── Photo.java
        │           │   ├── Category.java
        │           │   ├── Like.java
        │           │   ├── Rating.java
        │           │   └── Comment.java
        │           ├── dto/
        │           │   ├── UserRegistrationDto.java
        │           │   ├── PhotoUploadDto.java
        │           │   ├── ProfileUpdateDto.java
        │           │   └── CommentDto.java
        │           └── security/
        │               ├── CustomUserDetails.java
        │               └── CustomUserDetailsService.java
        └── resources/
            ├── application.properties               # MySQL database connection pool
            ├── schema.sql                           # Startup table builder
            ├── data.sql                             # Seeding roles/services/users
            ├── static/
            │   ├── css/
            │   │   └── style.css                    # Custom premium glassmorphism stylesheet
            │   ├── js/
            │   │   └── main.js                      # Client transitions & lightbox scripts
            │   └── uploads/                         # Folder hosting uploaded photos
            └── templates/
                ├── layout.html                      # Layout decorator
                ├── home.html                        # Welcome page
                ├── gallery.html                     # Paginated search/filters
                ├── login.html                       # Authenticate login
                ├── register.html                    # Registration form
                ├── photo-details.html               # Star rating details & comment threads
                ├── photographer-profile.html        # Public bio & portfolios
                ├── profile.html                     # Settings form
                ├── error.html                       # HTTP error catcher
                ├── dashboard/
                │   ├── home.html                    # Photographer dashboard & stats
                │   └── edit-photo.html              # Upload & update form
                └── admin/
                    ├── dashboard.html               # Admin overview stats
                    ├── users.html                   # Suspend/delete accounts
                    ├── photos.html                  # Delete photos
                    ├── categories.html              # Categories manager
                    └── comments.html                # Moderate comments
```

---

## 🔑 Seeding & Default Logins

On startup, `data.sql` automatically populates the database with:
*   **System Administrator**:
    *   **Email**: `admin@photoreview.com`
    *   **Password**: `admin123`
*   **Professional Photographer (Jane Doe)**:
    *   **Email**: `jane@photoreview.com`
    *   **Password**: `admin123`
*   **Regular Reviewer (Alex Reviewer)**:
    *   **Email**: `user@photoreview.com`
    *   **Password**: `admin123`

---

## 🛠️ Step-by-Step Setup Instructions

### 1. Prerequisites
Ensure you have the following installed on your machine:
*   **Java JDK 21**
*   **MySQL Server 8.x** (with root password set)
*   **Maven 3.x**

### 2. Database Creation
Create a database named `photo_review` in MySQL. Run this query in MySQL Workbench, MySQL Shell, or your preferred SQL terminal:
```sql
CREATE DATABASE IF NOT EXISTS photo_review;
```

### 3. Configure Database Credentials
Open the [application.properties](src/main/resources/application.properties) file and verify or update the connection settings to match your MySQL environment:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/photo_review?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Raj@2023#
```

### 4. Build and Compile the Application
Open a terminal in the project root directory (`c:\11111`) and compile the source code:
```powershell
mvn clean compile
```

### 5. Running the Application
Launch the embedded Tomcat server:
```powershell
mvn spring-boot:run
```
Once the console prints `Started PhotoReviewApplication in ... seconds`, navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 📝 Verification Flow
1.  **Browse Anonymously**: Go to `/gallery` and select category tags (e.g. Portrait) or search by name. Verify sorting by highest rating or most popular works.
2.  **Submit Rating, Comments, and Likes**:
    *   Log in at `/login` as `user@photoreview.com` with password `admin123`.
    *   Go to **Gallery** and click **Review** on a photo.
    *   Click the **Like** button. The likes count will increment.
    *   Select stars on the interactive rating bar. The average rating will update.
    *   Submit feedback in the **Submit Critique** textarea. The comment thread will show your post with a delete option.
3.  **Upload & Manage Photos (Photographer)**:
    *   Log in as `jane@photoreview.com` with password `admin123`.
    *   Go to **Dashboard**. Under **Photographer Console**, click **Upload New Photo**.
    *   Upload an image file and save. Go to the gallery to verify the image is active.
4.  **Moderate Content (Admin)**:
    *   Log in as `admin@photoreview.com` with password `admin123`.
    *   Navigate to **Dashboard** in the navbar to open the Admin Dashboard.
    *   Click **Manage Users** to suspend or activate accounts, or **Moderate Comments** / **Moderate Photos** to delete content.
