db.getSiblingDB('postcodes_db').Postcodes.createIndex({ postcode: 1 }, { unique: true });
