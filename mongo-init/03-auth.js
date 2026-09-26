const authDb = db.getSiblingDB('auth_db');

authDb.roles.createIndex({ name: 1 }, { unique: true });
authDb.roles.insertMany([
  { name: 'WEB_CLIENT', permissions: ['REGISTER', 'LOGIN'] },
  { name: 'USER', permissions: ['CALCULATE_DISTANCE', 'READ_POSTCODE', 'UPDATE_POSTCODE'] }
]);

authDb.principals.createIndex({ 'logins.type': 1, 'logins.login': 1 }, { unique: true });

// DEV ONLY: bcrypt (cost 10) of the client secret 'Test1234@'.
// Real environments create client accounts out of band, with secrets from a secret manager.
authDb.principals.insertOne({
  type: 'CLIENT',
  secretHash: '$2a$10$R06gKm162/3MUqyO/jC3E.pMNz0uh4NyI0HNJ3weRC.i4b6CJsDJ2',
  roles: ['WEB_CLIENT'],
  logins: [
    { type: 'CLIENT_ID', login: 'wcc-web' }
  ]
});

print(`auth_db: ${authDb.roles.countDocuments()} roles, ${authDb.principals.countDocuments()} principal(s) loaded`);
