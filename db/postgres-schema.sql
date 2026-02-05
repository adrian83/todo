-- PostgreSQL schema for Todo application
-- Optionally create database (run as a superuser):
--   CREATE DATABASE todo_app;
--   \c todo_app

-- Users
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password_hash TEXT NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP
);
CREATE UNIQUE INDEX ux_users_username ON users(username);
CREATE UNIQUE INDEX ux_users_email ON users(email);

-- Tags
CREATE TABLE tags (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP,
  CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX ix_tags_user_id ON tags(user_id);

-- Notes
CREATE TABLE notes (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP,
  CONSTRAINT fk_note_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX ix_notes_user_id ON notes(user_id);

-- Join table between notes and tags
CREATE TABLE note_tags (
  note_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  CONSTRAINT pk_note_tags PRIMARY KEY (note_id, tag_id),
  CONSTRAINT fk_note_tags_note FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE,
  CONSTRAINT fk_note_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);
CREATE INDEX ix_note_tags_note_id ON note_tags(note_id);
CREATE INDEX ix_note_tags_tag_id ON note_tags(tag_id);

-- Refresh tokens
CREATE TABLE refresh_tokens (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  token VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX ix_refresh_tokens_user_id ON refresh_tokens(user_id);

-- Optional: set owner or grant privileges if needed
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO todo_user;
