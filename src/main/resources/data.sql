INSERT INTO menu_item (name, description, image, note, price)
VALUES ('Bread', 'bread, egg', 'bread.jpg', 'no', 5.6) ON CONFLICT (name) DO NOTHING;
INSERT INTO menu_item (name, description, image, note, price)
VALUES ('Rice', 'rice, beef', 'rice.jpg', 'no', 10.6) ON CONFLICT (name) DO NOTHING;
INSERT INTO menu_item (name, description, image, note, price)
VALUES ('Noodle', 'noodle, beef', 'noodle.jpg', 'no', 2.6) ON CONFLICT (name) DO NOTHING;
INSERT INTO menu_item (name, description, image, note, price)
VALUES ('Beer', 'beer', 'beer.jpg', 'note', 5.1) ON CONFLICT (name) DO NOTHING;
INSERT INTO menu_item (name, description, image, note, price)
VALUES ('Water', 'aqua', 'water.jpg', 'no', 7.6) ON CONFLICT (name) DO NOTHING;