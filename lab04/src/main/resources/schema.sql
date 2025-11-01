-- ---
-- 1. DROP TABLES
-- Drop in reverse order of creation to avoid foreign key errors.
-- 'preferences' must be dropped first as it depends on 'students' and 'courses'.
-- ---
DROP TABLE IF EXISTS preferences;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS packs;
DROP TABLE IF EXISTS instructors;

-- ---
-- 2. CREATE TABLES
-- Create in order of dependency.
-- ---

-- These tables have NO foreign keys, so they are created FIRST.
CREATE TABLE instructors (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE packs (
                       id BIGSERIAL PRIMARY KEY,
                       year INT NOT NULL,
                       semester INT NOT NULL,
                       name VARCHAR(255) NOT NULL
);

-- This table depends on 'packs', so it comes AFTER 'packs'.
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) UNIQUE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          year INT NOT NULL,
                          pack_id BIGINT,

                          CONSTRAINT fk_student_pack
                              FOREIGN KEY(pack_id)
                                  REFERENCES packs(id)
                                  ON DELETE SET NULL
);

-- This table depends on 'instructors' and 'packs', so it comes AFTER both.
CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         type VARCHAR(20) NOT NULL CHECK (type IN ('compulsory', 'optional')),
                         code VARCHAR(50) UNIQUE NOT NULL,
                         abbr VARCHAR(20),
                         name VARCHAR(255) NOT NULL,
                         instructor_id BIGINT,
                         pack_id BIGINT,
                         group_count INT DEFAULT 1,
                         description TEXT,

                         CONSTRAINT fk_instructor
                             FOREIGN KEY(instructor_id)
                                 REFERENCES instructors(id)
                                 ON DELETE SET NULL,

                         CONSTRAINT fk_pack
                             FOREIGN KEY(pack_id)
                                 REFERENCES packs(id)
                                 ON DELETE SET NULL
);

-- This table depends on 'students' and 'courses', so it comes LAST.
CREATE TABLE preferences (
                             id BIGSERIAL PRIMARY KEY,
                             student_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             rank INT NOT NULL,

                             CONSTRAINT fk_preference_student
                                 FOREIGN KEY(student_id)
                                     REFERENCES students(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_preference_course
                                 FOREIGN KEY(course_id)
                                     REFERENCES courses(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT uk_student_course UNIQUE (student_id, course_id),
                             CONSTRAINT uk_student_rank UNIQUE (student_id, rank)
);