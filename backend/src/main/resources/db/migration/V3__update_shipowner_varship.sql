-- V3: Update Shipowner to Varship
UPDATE users SET email = 'info@varship.gr' WHERE id = 'b0000001-0000-0000-0000-000000000001';
UPDATE companies SET name = 'Varship Shipping Co.', description = 'Dynamic Greek shipping company specializing in tanker and bulk carrier operations.', website = 'https://varship.gr', fleet_size = 18, city = 'Piraeus' WHERE id = 'co000001-0000-0000-0000-000000000001';
