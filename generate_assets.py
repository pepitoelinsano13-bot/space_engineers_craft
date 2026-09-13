import os
import zlib
import struct
import json

def write_png(filename, width, height, pixels):
    raw_data = bytearray()
    for y in range(height):
        raw_data.append(0) # Filter type 0 (None)
        for x in range(width):
            r, g, b, a = pixels[y * width + x]
            raw_data.extend([r, g, b, a])
    
    compressed = zlib.compress(raw_data)
    
    png = bytearray(b'\x89PNG\r\n\x1a\n')
    
    # IHDR
    ihdr_data = struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0)
    ihdr_crc = zlib.crc32(b'IHDR' + ihdr_data)
    png.extend(struct.pack('>I', 13) + b'IHDR' + ihdr_data + struct.pack('>I', ihdr_crc))
    
    # IDAT
    idat_crc = zlib.crc32(b'IDAT' + compressed)
    png.extend(struct.pack('>I', len(compressed)) + b'IDAT' + compressed + struct.pack('>I', idat_crc))
    
    # IEND
    iend_crc = zlib.crc32(b'IEND')
    png.extend(struct.pack('>I', 0) + b'IEND' + struct.pack('>I', iend_crc))
    
    os.makedirs(os.path.dirname(filename), exist_ok=True)
    with open(filename, 'wb') as f:
        f.write(png)

def make_texture(primary, secondary, border, pattern="metal"):
    pixels = []
    for y in range(16):
        for x in range(16):
            if x == 0 or x == 15 or y == 0 or y == 15:
                pixels.append(border)
            elif (x + y) % 4 == 0 and pattern == "metal":
                pixels.append(secondary)
            elif pattern == "noise":
                if (x * 7 + y * 13) % 5 == 0:
                    pixels.append(secondary)
                else:
                    pixels.append(primary)
            elif pattern == "glow":
                dist = abs(x - 7.5) + abs(y - 7.5)
                if dist < 4:
                    pixels.append(secondary)
                else:
                    pixels.append(primary)
            elif pattern == "grill":
                if y % 2 == 0:
                    pixels.append(secondary)
                else:
                    pixels.append(primary)
            elif pattern == "solar":
                if x % 4 == 0 or y % 4 == 0:
                    pixels.append(border)
                else:
                    pixels.append(primary)
            else:
                pixels.append(primary)
    return pixels

base_dir = "/home/jose/Documents/antigravity/mysterious-hypatia/space_engineers_craft/src/main/resources"
block_tex_dir = os.path.join(base_dir, "assets/space_engineers_craft/textures/block")
item_tex_dir = os.path.join(base_dir, "assets/space_engineers_craft/textures/item")
blockstates_dir = os.path.join(base_dir, "assets/space_engineers_craft/blockstates")
block_models_dir = os.path.join(base_dir, "assets/space_engineers_craft/models/block")
item_models_dir = os.path.join(base_dir, "assets/space_engineers_craft/models/item")
lang_dir = os.path.join(base_dir, "assets/space_engineers_craft/lang")

blocks = [
    ("ship_core", (50, 60, 75, 255), (0, 200, 255, 255), (25, 30, 40, 255), "glow"),
    ("station_core", (40, 45, 55, 255), (255, 180, 0, 255), (20, 25, 30, 255), "metal"),
    ("cockpit", (30, 40, 50, 255), (100, 180, 230, 255), (15, 20, 25, 255), "glow"),
    ("ion_thruster", (45, 50, 60, 255), (0, 220, 255, 255), (20, 25, 30, 255), "glow"),
    ("hydrogen_thruster", (55, 50, 45, 255), (255, 120, 0, 255), (30, 25, 20, 255), "glow"),
    ("atmospheric_thruster", (60, 65, 70, 255), (180, 190, 200, 255), (30, 35, 40, 255), "metal"),
    ("rcs_thruster", (50, 55, 60, 255), (220, 230, 240, 255), (25, 30, 35, 255), "metal"),
    ("gyroscope", (45, 45, 50, 255), (210, 160, 40, 255), (25, 25, 30, 255), "glow"),
    ("gravity_generator", (35, 30, 45, 255), (180, 50, 240, 255), (20, 15, 25, 255), "glow"),
    ("air_vent", (55, 60, 65, 255), (0, 180, 220, 255), (30, 35, 40, 255), "grill"),
    ("hydrogen_electrolyzer", (45, 55, 65, 255), (240, 140, 30, 255), (25, 30, 35, 255), "metal"),
    ("satellite_core", (210, 170, 40, 255), (250, 220, 90, 255), (140, 110, 20, 255), "metal"),
    ("solar_array", (15, 40, 110, 255), (40, 90, 180, 255), (190, 200, 210, 255), "solar"),
    ("satellite_terminal", (35, 40, 48, 255), (0, 240, 160, 255), (20, 25, 30, 255), "glow"),
    ("lunar_regolith", (145, 148, 152, 255), (120, 122, 125, 255), (135, 138, 140, 255), "noise"),
    ("lunar_stone", (90, 92, 98, 255), (70, 72, 76, 255), (80, 82, 86, 255), "noise"),
    ("ares_sand", (185, 85, 50, 255), (160, 70, 40, 255), (170, 75, 45, 255), "noise"),
    ("ares_rock", (135, 50, 35, 255), (105, 38, 26, 255), (120, 44, 30, 255), "noise"),
    ("asteroid_stone", (45, 48, 52, 255), (30, 32, 35, 255), (40, 42, 45, 255), "noise")
]

for name, pri, sec, bor, pat in blocks:
    tex_file = os.path.join(block_tex_dir, f"{name}.png")
    pixels = make_texture(pri, sec, bor, pat)
    write_png(tex_file, 16, 16, pixels)
    
    # blockstate
    bs_file = os.path.join(blockstates_dir, f"{name}.json")
    with open(bs_file, 'w') as f:
        json.dump({
            "variants": {
                "": {"model": f"space_engineers_craft:block/{name}"}
            }
        }, f, indent=2)
        
    # block model
    bm_file = os.path.join(block_models_dir, f"{name}.json")
    with open(bm_file, 'w') as f:
        json.dump({
            "parent": "minecraft:block/cube_all",
            "textures": {
                "all": f"space_engineers_craft:block/{name}"
            }
        }, f, indent=2)
        
    # item model for block
    im_file = os.path.join(item_models_dir, f"{name}.json")
    with open(im_file, 'w') as f:
        json.dump({
            "parent": f"space_engineers_craft:block/{name}"
        }, f, indent=2)

items = [
    ("welder_tool", (60, 65, 75, 255), (0, 200, 255, 255), (30, 35, 45, 255), "glow"),
    ("oxygen_bottle", (20, 160, 220, 255), (180, 240, 255, 255), (10, 100, 140, 255), "glow"),
    ("hydrogen_bottle", (220, 120, 20, 255), (255, 190, 80, 255), (140, 70, 10, 255), "glow"),
    ("spacesuit_helmet", (230, 235, 240, 255), (240, 190, 40, 255), (180, 185, 190, 255), "glow"),
    ("spacesuit_chestplate", (230, 235, 240, 255), (0, 180, 240, 255), (180, 185, 190, 255), "metal"),
    ("spacesuit_leggings", (220, 225, 230, 255), (100, 110, 120, 255), (170, 175, 180, 255), "metal"),
    ("spacesuit_boots", (190, 195, 200, 255), (60, 70, 80, 255), (140, 145, 150, 255), "metal")
]

for name, pri, sec, bor, pat in items:
    tex_file = os.path.join(item_tex_dir, f"{name}.png")
    pixels = make_texture(pri, sec, bor, pat)
    write_png(tex_file, 16, 16, pixels)
    
    im_file = os.path.join(item_models_dir, f"{name}.json")
    with open(im_file, 'w') as f:
        json.dump({
            "parent": "minecraft:item/generated",
            "textures": {
                "layer0": f"space_engineers_craft:item/{name}"
            }
        }, f, indent=2)

# Lang files
en_lang = {
    "itemGroup.space_engineers_craft": "Space Engineers Craft",
    "block.space_engineers_craft.ship_core": "Ship Core",
    "block.space_engineers_craft.station_core": "Station Core",
    "block.space_engineers_craft.cockpit": "Cockpit",
    "block.space_engineers_craft.ion_thruster": "Ion Thruster",
    "block.space_engineers_craft.hydrogen_thruster": "Hydrogen Thruster",
    "block.space_engineers_craft.atmospheric_thruster": "Atmospheric Thruster",
    "block.space_engineers_craft.rcs_thruster": "RCS Thruster",
    "block.space_engineers_craft.gyroscope": "Gyroscope",
    "block.space_engineers_craft.gravity_generator": "Artificial Gravity Generator",
    "block.space_engineers_craft.air_vent": "Air Vent",
    "block.space_engineers_craft.hydrogen_electrolyzer": "Hydrogen Electrolyzer",
    "block.space_engineers_craft.satellite_core": "Satellite Core",
    "block.space_engineers_craft.solar_array": "Solar Array",
    "block.space_engineers_craft.satellite_terminal": "Satellite Terminal",
    "block.space_engineers_craft.lunar_regolith": "Lunar Regolith",
    "block.space_engineers_craft.lunar_stone": "Lunar Stone",
    "block.space_engineers_craft.ares_sand": "Ares Sand",
    "block.space_engineers_craft.ares_rock": "Ares Rock",
    "block.space_engineers_craft.asteroid_stone": "Asteroid Stone",
    "item.space_engineers_craft.welder_tool": "Welder Tool",
    "item.space_engineers_craft.oxygen_bottle": "Oxygen Bottle",
    "item.space_engineers_craft.hydrogen_bottle": "Hydrogen Bottle",
    "item.space_engineers_craft.spacesuit_helmet": "EVA Spacesuit Helmet",
    "item.space_engineers_craft.spacesuit_chestplate": "EVA Spacesuit Chestplate",
    "item.space_engineers_craft.spacesuit_leggings": "EVA Spacesuit Leggings",
    "item.space_engineers_craft.spacesuit_boots": "EVA Spacesuit Boots",
    "entity.space_engineers_craft.physical_ship": "Physical Space Ship",
    "entity.space_engineers_craft.satellite": "Orbital Telemetry Satellite"
}

es_lang = {
    "itemGroup.space_engineers_craft": "Space Engineers Craft",
    "block.space_engineers_craft.ship_core": "Núcleo de Nave Espacial",
    "block.space_engineers_craft.station_core": "Núcleo de Estación Espacial",
    "block.space_engineers_craft.cockpit": "Cabina de Mando (Cockpit)",
    "block.space_engineers_craft.ion_thruster": "Propulsor Iónico",
    "block.space_engineers_craft.hydrogen_thruster": "Propulsor de Hidrógeno",
    "block.space_engineers_craft.atmospheric_thruster": "Propulsor Atmosférico",
    "block.space_engineers_craft.rcs_thruster": "Propulsor de Maniobra RCS",
    "block.space_engineers_craft.gyroscope": "Giroscopio de Par Inercial",
    "block.space_engineers_craft.gravity_generator": "Generador de Gravedad Artificial",
    "block.space_engineers_craft.air_vent": "Rejilla de Ventilación (Soporte Vital)",
    "block.space_engineers_craft.hydrogen_electrolyzer": "Electrolizador de Hidrógeno y Oxígeno",
    "block.space_engineers_craft.satellite_core": "Núcleo de Satélite Desplegable",
    "block.space_engineers_craft.solar_array": "Panel Solar Espacial",
    "block.space_engineers_craft.satellite_terminal": "Terminal de Telemetría Orbital",
    "block.space_engineers_craft.lunar_regolith": "Regolito Lunar",
    "block.space_engineers_craft.lunar_stone": "Roca Lunar",
    "block.space_engineers_craft.ares_sand": "Arena de Ares (Marte)",
    "block.space_engineers_craft.ares_rock": "Roca de Ares (Marte)",
    "block.space_engineers_craft.asteroid_stone": "Roca de Asteroide",
    "item.space_engineers_craft.welder_tool": "Herramienta de Soldadura (Welder)",
    "item.space_engineers_craft.oxygen_bottle": "Botella de Oxígeno Presurizada",
    "item.space_engineers_craft.hydrogen_bottle": "Botella de Combustible Hidrógeno",
    "item.space_engineers_craft.spacesuit_helmet": "Casco de Traje Espacial EVA",
    "item.space_engineers_craft.spacesuit_chestplate": "Torso de Traje Espacial EVA",
    "item.space_engineers_craft.spacesuit_leggings": "Pantalones de Traje Espacial EVA",
    "item.space_engineers_craft.spacesuit_boots": "Botas Magnéticas de Traje Espacial EVA",
    "entity.space_engineers_craft.physical_ship": "Nave Espacial Física",
    "entity.space_engineers_craft.satellite": "Satélite Orbital de Telemetría"
}

with open(os.path.join(lang_dir, "en_us.json"), 'w', encoding='utf-8') as f:
    json.dump(en_lang, f, indent=2, ensure_ascii=False)

with open(os.path.join(lang_dir, "es_es.json"), 'w', encoding='utf-8') as f:
    json.dump(es_lang, f, indent=2, ensure_ascii=False)

print("Generated all assets, textures, models, and lang files successfully!")
