const authDb = db.getSiblingDB('auth_db');

authDb.roles.createIndex({ name: 1 }, { unique: true });
authDb.principals.createIndex({ 'logins.type': 1, 'logins.login': 1 }, { unique: true });

authDb.roles.insertMany([
  { name: 'WEB_CLIENT', permissions: ['REGISTER', 'LOGIN'] },
  { name: 'USER', permissions: ['CALCULATE_DISTANCE', 'READ_POSTCODE', 'UPDATE_POSTCODE', 'READ_OWN_PROFILE'] },
  { name: 'USER_SERVICE', permissions: ['CREATE_PRINCIPAL', 'DELETE_PRINCIPAL'] }
]);

// DEV ONLY: bcrypt (cost 10) of the client secret 'Test1234@', shared by both dev clients.
// Real environments create client accounts out of band, with secrets from a secret manager.
const DEV_SECRET_HASH = '$2a$10$R06gKm162/3MUqyO/jC3E.pMNz0uh4NyI0HNJ3weRC.i4b6CJsDJ2';

authDb.principals.insertMany([
  {
    type: 'CLIENT',
    secretHash: DEV_SECRET_HASH,
    roles: ['WEB_CLIENT'],
    logins: [{ type: 'CLIENT_ID', login: 'wcc-web' }]
  },
  {
    type: 'CLIENT',
    secretHash: DEV_SECRET_HASH,
    roles: ['USER_SERVICE'],
    logins: [{ type: 'CLIENT_ID', login: 'user-service' }]
  }
]);

print(`auth_db: ${authDb.roles.countDocuments()} roles, ${authDb.principals.countDocuments()} principals loaded`);
