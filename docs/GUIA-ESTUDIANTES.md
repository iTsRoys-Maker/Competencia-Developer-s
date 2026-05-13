# Guía para el grupo — del repo al WhatsApp

## PASO 1 — Crear cuenta en GitHub (si no tienen)

1. Ir a https://github.com → **Sign up**
2. Usar email personal o institucional
3. Elegir un username con su nombre real: `juan-perez` no `xXdarkcoderXx`

---

## PASO 2 — Crear el repositorio del proyecto

Solo **un integrante del grupo** hace esto:

1. Ir a https://github.com → clic en **"+"** arriba a la derecha → **New repository**
2. Configurar así:

   | Campo | Valor |
   |-------|-------|
   | Repository name | `ecommerce-[nombre-proyecto]` (ej. `ecommerce-shopfast`) |
   | Description | Nombre del grupo y curso |
   | Visibility | ✅ **Public** (obligatorio para que el profesor lo vea) |
   | Add a README | ✅ Marcar |
   | Add .gitignore | Java |

3. Clic en **Create repository**

---

## PASO 3 — Agregar a los compañeros como colaboradores

El dueño del repo hace esto para que todos puedan hacer push:

1. Ir al repo → **Settings** → **Collaborators** → **Add people**
2. Buscar el username de cada compañero y agregarlos
3. Cada compañero acepta la invitación que llega a su email

---

## PASO 4 — Clonar el repo en cada computador

Cada integrante ejecuta esto en su terminal (Git Bash en Windows):

```bash
git clone https://github.com/[usuario-dueño]/ecommerce-[nombre-proyecto].git
cd ecommerce-[nombre-proyecto]
```

>  Si nunca han usado Git: descargar desde https://git-scm.com/downloads

---

## PASO 5 — Estructura sugerida del proyecto

No hay una estructura obligatoria, pero sí hay un mínimo:

```
ecommerce-[proyecto]/
├── src/                  ← todo el código Java aquí
├── docs/
│   └── uml/              ← diagramas UML como PNG
├── assets/               ← capturas de pantalla
├── db/                   ← script SQL (si usan BD)
├── .gitignore
└── README.md             ← completen este archivo
```

---

## PASO 6 — Reglas del proyecto

### Tecnología
- El núcleo del sistema **debe estar en Java**
- Pueden usar cualquier framework o librería compatible:
  - UI: Swing, JavaFX, Spring Boot + Thymeleaf, Spring Boot + API REST + frontend
  - BD: Archivos, SQLite, MySQL, H2, JPA/Hibernate
  - Build: Maven, Gradle, o sin build tool
- **Spring Boot cuenta**, siempre que las entidades del modelo estén en Java con POO real

### POO obligatorio (se evalúa en el código)
El proyecto debe mostrar de forma visible:

| Concepto | Ejemplo mínimo aceptable |
|----------|--------------------------|
| **Herencia** | `Cliente extends Usuario`, `ProductoFisico extends Producto` |
| **Encapsulación** | Atributos `private` + getters/setters en las entidades |
| **Polimorfismo** | `@Override` en al menos un método funcional |
| **Abstracción** | Clase `abstract` o `interface` aplicada en el flujo real |
| **Colecciones** | `ArrayList` u otra colección para listas de productos/pedidos |
| **Excepciones** | `try/catch` en operaciones que pueden fallar |

### Funcionalidades mínimas
- CRUD de productos con control de stock
- Registro/login de usuarios (cliente y admin)
- Carrito de compras
- Flujo completo: carrito → confirmar → pago → pedido generado
- Historial de pedidos del cliente
- Interfaz gráfica o web funcional (no solo consola)
- Datos que persistan (no se borran al cerrar la app)

---

## PASO 7 — Flujo de trabajo en equipo con Git

```bash
# Antes de empezar a trabajar cada día — traer los cambios del repo
git pull origin main

# Después de trabajar — guardar y subir
git add .
git commit -m "descripción corta de lo que hicieron"
git push origin main
```

> 💡 Hagan commits frecuentes y con mensajes claros.
> "fix bug" no sirve. "Corrige validación de stock en Carrito" sí.

---

## PASO 8 — Agregar los diagramas UML

1. Crear el diagrama en https://java-learn-test.netlify.app → Laboratorio → UML
2. Exportar como PNG
3. Guardarlo en `docs/uml/diagrama-clases.png`
4. Hacer commit y push

---

## PASO 9 — Completar el README

Editar `README.md` con:
- Integrantes reales con sus usuarios de GitHub
- Descripción del proyecto
- Instrucciones para ejecutarlo (otro estudiante debe poder correrlo leyendo solo el README)
- Tecnologías usadas
- Capturas en `assets/` referenciadas en el README

---

## PASO 10 — Compartir en el grupo de WhatsApp

Cuando el proyecto esté listo, **un integrante del grupo** manda el link con este formato exacto:

```
👉 Grupo [N] — [Nombre del proyecto] — https://github.com/usuario/repo
```

Ejemplo:
```
👉 Grupo 3 — ShopFast — https://github.com/maria-garcia/ecommerce-shopfast
```

---

## ⚠️ Errores frecuentes

| Error | Qué hacer |
|-------|-----------|
| `Permission denied` al hacer push | Asegúrate de que el dueño te agregó como colaborador (Paso 3) |
| `rejected – non-fast-forward` | Ejecuta `git pull origin main` primero, luego vuelve a hacer push |
| `fatal: not a git repository` | Estás en la carpeta equivocada — entra a la carpeta del repo clonado |
| El README no muestra las imágenes | La ruta debe ser relativa: `docs/uml/diagrama-clases.png` (sin `/` al inicio) |
| Subí mis credenciales de BD | Elimina el archivo, haz commit con el cambio, y cambia la contraseña |
