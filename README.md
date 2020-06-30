Minnie
-------

Minnie is a simple tool to split a folder into many smaller folders.

- input: cartella di file (solitamente immagini, ma non è sempre detto) 
- output: voglio ottenere n cartelle che contengono ciascuna x file della cartella di input 
- dove x può essere un numero intero variabile che fisso di volta in volta

## Usage

```
$ java -jar Minnie-0.2.jar 

Error: Missing option --src
Error: Missing option --size
Minnie 0.1
Usage: minnie [options]

  -s, --src <value>   source directory to split
  -d, --dest <dest>   an existing path where to create destination folders
  -s, --size <value>  maximum count for <libname>
  -c, --copy          set to copy the src files into dest
  -m, --move          set to move the src files into dest
  --prefix <value>    set the prefix name of subdirectories - eg. <prefix>50-60

Example:
java -jar Minnie-0.1.jar --src images/ --dest out/ --size 50
java -jar Minnie-0.1.jar -s images/ -d out/ -s 50
java -jar Minnie-0.1.jar --src images/ -d out/ --size 30 --move
```
