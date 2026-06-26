from PIL import Image, ImageDraw

sizes = {
    "hdpi": 72,
    "mdpi": 48,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192,
}

for density, px in sizes.items():
    img = Image.new("RGBA", (px, px), (25, 118, 210, 255))
    draw = ImageDraw.Draw(img)
    m = px // 6
    draw.ellipse([m, m, px - m, px - m], fill=(255, 255, 255, 200))
    path = f"app/src/main/res/mipmap-{density}/ic_launcher.png"
    img.save(path)
    print(f"  Created {path} ({px}x{px})")
