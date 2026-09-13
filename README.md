# Space Engineers Craft (NeoForge 1.21.1)

Un mod de ingeniería aeroespacial, física orbital 6DOF y terreno orgánico para Minecraft 1.21.1 NeoForge. Inspirado en mecánicas de *Space Engineers* y diseñado para ser **100% compatible con Create, TFMG y Modern Foundry**.

---

## 🚀 Características Principales

### 1. Física de Terreno Orgánico (*Smooth Slopes*)
* **Subida continua de pendientes**: El jugador asciende colinas, montañas y dunas suavemente sin saltar escalones (`step-assist` dinámico en bloques de terreno natural).
* **Compatibilidad intacta**: Los chunks siguen almacenando bloques normales, permitiendo que Create (ejes, engranajes), TFMG (tuberías, refinerías) y Modern Foundry funcionen sin ninguna incompatibilidad.

### 2. Construcción Libre y Rejillas Físicas (*Physics Grids & 6DOF*)
* **Ensamblado con Soldador**: Diseña cualquier estructura de bloques, coloca un **Ship Core** o **Station Core**, y haz clic derecho con el **Soldador (Welder Tool)** para compilarla en una **Nave Espacial Física**.
* **Control Vectorial 6DOF**: Siéntate en la **Cabina de Mando (Cockpit)** para pilotar la nave:
  * `[W / S]`: Empuje hacia adelante / reversa.
  * `[A / D]`: Alabeo / Guiñada lateral (strafe).
  * `[Espacio / Shift]`: Ascenso y descenso vectorial.
  * `[Shift + Clic]`: Desacopla la nave y la vuelve a anclar a la rejilla de bloques del mundo.
* **Sistemas de Propulsión**:
  * **Propulsor Iónico de Plasma**: Máxima eficiencia en vacío espacial (emite llamaradas azules).
  * **Propulsor de Hidrógeno**: Empuje masivo (requiere botellas de hidrógeno generadas en el electrolizador).
  * **Propulsor Atmosférico**: Diseñado para vuelo en atmósfera planetaria.
  * **Propulsores RCS**: Micropropulsores de maniobra y estabilidad torsional.
  * **Giroscopio Inercial**: Aumenta la velocidad de rotación y estabilidad angular de la nave.

### 3. Soporte Vital, Presurización y Gravedad Artificial
* **Rejilla de Ventilación (Air Vent)**: Detecta si una sala de la nave o estación está sellada herméticamente. En salas presurizadas suministra oxígeno respirable al 100%.
* **Generador de Gravedad**: Genera un campo de gravedad artificial de 1.0G para caminar normalmente en gravedad cero.
* **Electrolizador H2/O2**: Disocia cubos de agua en botellas presurizadas de Hidrógeno y Oxígeno.
* **Traje Espacial EVA Completo**: Casco, Pechera con tanque, Pantalones y Botas Magnéticas para resistir el vacío del espacio y las temperaturas extremas.

### 4. Cuerpos Celestes y Dimensiones Espaciales
* **Órbita Baja Terrestre (`space_engineers_craft:earth_orbit`)**: Cruza los $Y \ge 600$ de altura para escapar de la atmósfera terrestre y entrar en órbita con microgravedad.
* **Luna (`space_engineers_craft:luna`)**: Gravedad lunar ($0.16G$), regolito y basalto lunar.
* **Marte / Ares (`space_engineers_craft:ares`)**: Gravedad marciana ($0.38G$), atmósfera tenue y tormentas de arena roja.
* **Cinturón de Asteroides (`space_engineers_craft:asteroid_belt`)**: Rocas ricas en minerales flotando en el vacío.
* **Red de Satélites Autónomos**: Lanza satélites con paneles solares y monitoriza el radar y telemetría desde el **Terminal de Satélites**.

---

## 🛠️ Instalación

1. Asegúrate de tener **Minecraft 1.21.1** con **NeoForge (v21.1.243+)**.
2. Coloca `space_engineers_craft-1.0.0.jar` en la carpeta `mods` de tu cliente o servidor.
3. ¡Despega hacia las estrellas!
