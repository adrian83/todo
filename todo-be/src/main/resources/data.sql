
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  email VARCHAR(250) NOT NULL,
  password_hash VARCHAR(500) NOT NULL
);

ALTER TABLE users
ADD CONSTRAINT IF NOT EXISTS email_unique
UNIQUE ( email );

CREATE TABLE IF NOT EXISTS todos (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  text VARCHAR(250) NOT NULL,
  user_id INT NOT NULL
);