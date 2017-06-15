Slick vs Doobie
==========

[license-badge]: https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square
[license-url]: LICENSE


## Slick
This is the [starting point](https://github.com/fagossa/xke-slick_doobie/tree/master/src/main/scala/com/xebia/slick) for slick.

## Doobie
This is the [starting point](https://github.com/fagossa/xke-slick_doobie/tree/master/src/main/scala/com/xebia/doobie) for doobie.

## Starting the db
First enter to the sbt terminal
```
sbt
```

Then, you could use docker to start a pg server
```
$ startPGTask 
```
Or to stop it

```
$ stopPGTask 
```

Once the pg server has started and if you only want to run the test you could use

```
$ testOnly
```

On the contrary if you want to run the db, execute the test and stop everything at the end

```
sbt test
```

