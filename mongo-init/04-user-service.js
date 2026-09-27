// Lesson 9: user-service's client account, its role, and user_db's index. Safe to run more than once.
const authDb = db.getSiblingDB('auth_db');

authDb.roles.updateOne(
  { name: 'USER_SERVICE' },
  { $set: { permissions: ['CREATE_PRINCIPAL', 'DELETE_PRINCIPAL'] } },
  { upsert: true }
);
authDb.roles.updateOne({ name: 'USER' }, { $addToSet: { permissions: 'READ_OWN_PROFILE' } });

// DEV ONLY: bcrypt (cost 10) of the client secret 'user-service-secret'.
if (!authDb.principals.findOne({ logins: { $elemMatch: { type: 'CLIENT_ID', login: 'user-service' } } })) {
  authDb.principals.insertOne({
    type: 'CLIENT',
    secretHash: '$2a$10$R06gKm162/3MUqyO/jC3E.pMNz0uh4NyI0HNJ3weRC.i4b6CJsDJ2',
    roles: ['USER_SERVICE'],
    logins: [
      { type: 'CLIENT_ID', login: 'user-service' }
    ]
  });
}

db.getSiblingDB('user_db').users.createIndex({ principalId: 1 }, { unique: true });

print('user-service seed applied');
