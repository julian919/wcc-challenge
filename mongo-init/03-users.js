const userDb = db.getSiblingDB('user_db');

userDb.users.createIndex({ principalId: 1 }, { unique: true });

print('user_db: index created');
