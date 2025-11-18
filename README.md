<p align="center">
  <a href="https://github.com/jestebanflorez10/SaucePOO">
    <img src="https://github.com/jestebanflorez10/SaucePOO/blob/main/documentacion/Sauce_Logo.png" alt="Logo Proyecto" />
  </a>
</p>
<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/version-prerelease%200.3-yellow" alt="Version"/></a>
  <a href="https://www.java.com"><img src="https://img.shields.io/badge/language-Java-red" alt="Language"/></a>
  <a href="#"><img src="https://img.shields.io/badge/packaging-jar-blue" alt="Packaging"/></a>
</p>

---

Sauce es un proyecto de software en Java con interfaz grafica (gracias a la librería Swing) para la gestión de pequeñas pizzerías. El software automatizará los procesos de pedidos, facturación e inventario de la pizzeria. Sauce contara con dos tipos de usuario: administradores y cajeros, quienes podrán gestionar eficientemente las operaciones diarias del negocio, optimizando la atención al cliente y reduciendo errores operativos en el punto de venta.
## ¿Cómo instalar Sauce?
El proyecto es un proyecto desarrollado con el [IDE de Netbeans](https://netbeans.apache.org/front/main/download/nb26/) en su versión 26 y con el gestor de proyectos [Apache Maven](https://maven.apache.org/download.cgi) usando el Java Development Kit o [JDK 24](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html), para conseguir el ejecutrable .jar del proyecto puede usar el comando de maven desde la carpeta donde usted tenga guardado el pom.xml junto al proyecto:
```bash
# Abra un terminal en su sistema operativo
# Asegure de tener git instado
# De lo contrario visite https://git-scm.com para instalar Git en su dispositivo

# Descargue (Clone) este repositorio
git clone https://github.com/jestebanflorez10/SaucePOO.git

# Navege hasta la carpeta raíz del proyecto
cd SaucePOO

# Recuerde revisar que Java este instalado
java -version
# Visite el sitio de descargas oficiales de Java para instalarlo

#También revise si tiene Maven instalado
mvn -version
#Si el comando no funciona, visite la web de Maven

#Obtenga el instalador del proyecto a partir del pom.xml
maven clean package
```
Este comando le devolvera un archivo en formato JAR en el directorio /target/ de la forma SaucePOO-prerelase 0.3.jar (el nombre SaucePOO + algún codigo de la versión)
## Uso
Para usar el ejecutable de Sauce construido anteriormente, ejecutelo con su instalación de Java de su dispositivo
```bash
java -jar "SaucePOO-prerelase 0.3.jar"
```
Al ejecutar el programa, este creara un directorio /database/ para guardar la base de datos de SQLite usuarios.db y un directorio /facturas/ para guardar en formato PDF las facturas creadas
<p align="center">
  <a href="https://github.com/jestebanflorez10/SaucePOO">
    <img src="https://github.com/jestebanflorez10/SaucePOO/blob/main/documentacion/LogInDEMO.png" alt="Interfaz de Inicio de sesión" />
  </a>
</p>

---
