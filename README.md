# XML Report transforming Tool

Tool to transform report files to desired output format (currently only wiki output format supported)

##### Build:
(requires Java 8 or higher)
```
mvn clean install
```

##### Usage by example:

###### UPLOAD of n randomly generated files (key = UUID), each 2kB size
```
java -jar target/reporttransformer.jar -i <input directory> -o <output directory> --outputFormat <wiki> -t <number of threads>
```

###### General usage:
```
java -jar reporttransformer.jar [options...]

 -i (--input) VAL     : input directory by default current directory
 -o (--output) VAL    : output directory by default current directory
 --outputFormat VAL   : output format by default wiki (will be support others in future)
 -t (--threads) N     : number of threads (default: 2)
```
