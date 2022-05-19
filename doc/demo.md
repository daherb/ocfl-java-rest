# Demo

## Start server

To start the server the maven command line can be user:

```
mvn compile exec:java
```

While starting, the server checks if the two folders `ocfl-repo` and `ocfl-work` exist and creates them if they are missing.
The main OCFL store will be placed in `ocfl-repo` and `ocfl-work` is used to assemble a version of an object. Initially the OCFL
store contains some metadata files but no objects yet.

```
% find ocfl-repo 
ocfl-repo
ocfl-repo/0004-hashed-n-tuple-storage-layout.md
ocfl-repo/extensions
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout/config.json
ocfl-repo/ocfl_1.0.txt
ocfl-repo/ocfl_extensions_1.0.md
ocfl-repo/ocfl_layout.json
ocfl-repo/0=ocfl_1.0
% curl localhost:8080/list_objects
[]
```

## Create objects

To create an object we have to prepare the files to be added. We can start any folder containing whatever files we want, for example:

```
% find /tmp/ocfl 
/tmp/ocfl
/tmp/ocfl/hmat
/tmp/ocfl/hmat/hmat.zip
```

Based on these files and folders we can create objects by calling the URL `/put_object` with a list of parameters (see [documentation.md](documentation.md) for details):

```
% curl 'localhost:8080/put_object?object_id=ids:hmat&path=/tmp/ocfl/hmat&name=Herbert%20Lange&address=mailto:lange@ids-mannheim.de&message=New%20object%20for%20hmat'
{
   "created" : 1652973501.06626,
   "files" : [
      {
         "fixity" : {
            "sha512" : "0bb13a7d95f6ca073cc3437f656883316c859534d9a6315585122e08950b2bf995c8c424cfc16e8b3c3c11af91648827049f952bbb22068d7624f604b38fdc3a"
         },
         "path" : "hmat.zip",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content/hmat.zip"
      }
   ],
   "mutable" : false,
   "objectId" : "ids:hmat",
   "objectVersionId" : {
      "objectId" : "ids:hmat",
      "versionNum" : "v1"
   },
   "versionInfo" : {
      "created" : 1652973501.06626,
      "message" : "New object for hmat",
      "user" : {
         "address" : "mailto:lange@ids-mannheim.de",
         "name" : "Herbert Lange"
      }
   },
   "versionNum" : "v1"
}

% find ocfl-repo
ocfl-repo
ocfl-repo/0004-hashed-n-tuple-storage-layout.md
ocfl-repo/extensions
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout/config.json
ocfl-repo/ocfl_1.0.txt
ocfl-repo/e3
ocfl-repo/e3/63
ocfl-repo/e3/63/f2
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/0=ocfl_object_1.0
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/inventory.json
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content/hmat.zip
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/inventory.json.sha512
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/inventory.json
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/inventory.json.sha512
ocfl-repo/ocfl_extensions_1.0.md
ocfl-repo/ocfl_layout.json
ocfl-repo/0=ocfl_1.0

% curl localhost:8080/validate_objects                
{"ids:hmat":{"errors":[],"warnings":[],"infos":[]}}
```

As a result we now have a new folder structure in the store starting with `e3` and ending in `e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42`. The first three levels are just the prefix of the latter which itself is a hash of the object identifier. We can see the file `hmat.zip` we added in the `v1/content` subfolder.

## Access objects

To access objects we can first check which objects are in the store by accessing the `/list_objects` URL. After making sure that the object exists, we can copy its content to a path given as a parameter to the URL `/get_object`, which copies the files to the destination and returns the object information. We can now check that the hash values from the store match both the initial file and the restored file.

```
% curl localhost:8080/list_objects
["ids:hmat"]

% curl 'localhost:8080/get_object?object_id=ids:hmat&path=/tmp/ocfl/hmat-update'
{
   "created" : 1652973501.06626,
   "files" : [
      {
         "fixity" : {
            "sha512" : "0bb13a7d95f6ca073cc3437f656883316c859534d9a6315585122e08950b2bf995c8c424cfc16e8b3c3c11af91648827049f952bbb22068d7624f604b38fdc3a"
         },
         "path" : "hmat.zip",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content/hmat.zip"
      }
   ],
   "mutable" : false,
   "objectId" : "ids:hmat",
   "objectVersionId" : {
      "objectId" : "ids:hmat",
      "versionNum" : "v1"
   },
   "versionInfo" : {
      "created" : 1652973501.06626,
      "message" : "New object for hmat",
      "user" : {
         "address" : "mailto:lange@ids-mannheim.de",
         "name" : "Herbert Lange"
      }
   },
   "versionNum" : "v1"
}

% find /tmp/ocfl             
/tmp/ocfl
/tmp/ocfl/hmat
/tmp/ocfl/hmat/hmat.zip
/tmp/ocfl/hmat-update
/tmp/ocfl/hmat-update/hmat.zip

% sha512sum /tmp/ocfl/hmat/hmat.zip /tmp/ocfl/hmat-update/hmat.zip
0bb13a7d95f6ca073cc3437f656883316c859534d9a6315585122e08950b2bf995c8c424cfc16e8b3c3c11af91648827049f952bbb22068d7624f604b38fdc3a  /tmp/ocfl/hmat/hmat.zip
0bb13a7d95f6ca073cc3437f656883316c859534d9a6315585122e08950b2bf995c8c424cfc16e8b3c3c11af91648827049f952bbb22068d7624f604b38fdc3a  /tmp/ocfl/hmat-update/hmat.zip

```

## Update objects

After restoring an object we can update its content and update the object in the store based on these changes. For example we can extract the ZIP archive we added in version 1. To update we simply use the `/put_object` URL again. 

```
% cd /tmp/ocfl/hmat-update
% unzip hmat.zip
% find /tmp/ocfl
% find /tmp/ocfl           
/tmp/ocfl
/tmp/ocfl/hmat
/tmp/ocfl/hmat/hmat.zip
/tmp/ocfl/hmat-update
/tmp/ocfl/hmat-update/hmat
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri.zip
/tmp/ocfl/hmat-update/hmat/corpus-utilities
/tmp/ocfl/hmat-update/hmat/corpus-utilities/FormatTable4BasicTranscription.xsl
/tmp/ocfl/hmat-update/hmat/corpus-utilities/annotation-specification-disfluency.xml
/tmp/ocfl/hmat-update/hmat/corpus-utilities/AnnotationPanel_STTS_folk.xml
/tmp/ocfl/hmat-update/hmat/corpus-materials
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_2b.pdf
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_1a.png
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_1b.pdf
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_2b.png
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_2a.pdf
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_2a.png
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_1a.pdf
/tmp/ocfl/hmat-update/hmat/corpus-materials/MapTask_1b.png
/tmp/ocfl/hmat-update/hmat/hamatac.coma
/tmp/ocfl/hmat-update/hmat/documentation
/tmp/ocfl/hmat-update/hmat/documentation/HAMATAC.pdf
/tmp/ocfl/hmat-update/hmat/documentation/Disfluency_Annotation_Guidelines.pdf
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri_s.exs
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.BMP
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.mp3
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.exb
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.wav
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.wav
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.mp3
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali_s.exs
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.BMP
/tmp/ocfl/hmat-update/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.exb
/tmp/ocfl/hmat-update/hmat.zip
[...]

% curl 'localhost:8080/put_object?object_id=ids:hmat&path=/tmp/ocfl/hmat-update&name=Herbert%20Lange&address=mailto:lange@ids-mannheim.de&message=Update%20of%20hmat'
{
   "created" : 1652974796.63851,
   "files" : [
      {
         "fixity" : {
            "sha512" : "663ed6962dcd75a8683aeec59a15d343488cee1fa1274447ea531189dfd58f6aa3910f8faf41c8bab73df3bba7dcc253cf9eff211f756a207aa39f39fa0688d3"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri_s.exs",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri_s.exs"
      },
      {
         "fixity" : {
            "sha512" : "a13590d8d0aaff2d9933a8ca64e8ce0c5473dc8cac716f87b7bc630094449fb3326c250b9d94cd0949ef4836b6317096664497058a5888011718f57938333001"
         },
         "path" : "hmat/documentation/HAMATAC.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/documentation/HAMATAC.pdf"
      },
      {
         "fixity" : {
            "sha512" : "0f42295875ed007b4021417fb158baf1883db83ac87148d235aebb1087a8361123b358f9c916eb1a4859fa5b5fa401b7c499e16a7c86ac83e8dc82a8f76e58ee"
         },
         "path" : "hmat/corpus-materials/MapTask_2b.png",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2b.png"
      },
      {
         "fixity" : {
            "sha512" : "5385dc0e6566b30ca004b53c2f62ae4f253b4831cae05a880d4707e9f4957b709e0bff1605bf75b01f8b107c12c520835549910f5f8022aaf7ab9b1e51dd4b55"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.exb",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.exb"
      },
      {
         "fixity" : {
            "sha512" : "d0f419378b31a3313a7f96be65e293d1a6242057f670ad5071bd4698d990b5851d902b7ea67a6eb859e6c5637f4bb283ed12e0a29ec00a175a4083dbabfdd93d"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.BMP",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.BMP"
      },
      {
         "fixity" : {
            "sha512" : "5cb928ab33602b3ac6741168724311abb787286182922cf500bae3057d83870faf3ab1a2297fc7e1698d1185a6c9ddac7a84f6e4fb524b1084b6f01aaf0b32a2"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.wav",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.wav"
      },
      {
         "fixity" : {
            "sha512" : "b18b500a25423fe1367c5fd21e8a1ea516245eb31a78025fdead4c25993f639b26e6467ab93177f512cf24391a597faa6b56a8a0c2b7da83b5114798567e3240"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.exb",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.exb"
      },
      {
         "fixity" : {
            "sha512" : "197519ec09ffe96c1b40beb9770c6256bf55f369e5b180adecf45be4335d9e39f0d9e58a7239211ac1eff2b9ea076bf8185731ce4a916b013ba71eaf2c128f5e"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.BMP",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.BMP"
      },
      {
         "fixity" : {
            "sha512" : "53dc67ecfc930ae4e586bd89045f3ea254e25ff894a61c785bd5248d16ff5923479dc5f31942f19cd0ce970b98008c753892b12375660b81acb293eff7ec1b29"
         },
         "path" : "hmat/corpus-materials/MapTask_2a.png",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2a.png"
      },
      {
         "fixity" : {
            "sha512" : "fad7488aa6d1e34640e21c07854ae6184b3363a5032b6afc6ef403549ecbb20a0386c56e7ab823f624c9e40134022ef59b19e31060626b4d15e258264277e266"
         },
         "path" : "hmat/corpus-utilities/AnnotationPanel_STTS_folk.xml",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/AnnotationPanel_STTS_folk.xml"
      },
      {
         "fixity" : {
            "sha512" : "d84b663c31e6b234aacad10200414075e53e3a417eebcac07d6baac3493761704c1b7135a533dab21563ba091974c2879a555bac1d9de8ac2c407039ad1a9778"
         },
         "path" : "hmat/corpus-materials/MapTask_1a.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1a.pdf"
      },
      {
         "fixity" : {
            "sha512" : "8c8438dbc15896cb3045428690fd7e7f326126e3a116c4e4b481da89b5d7c32fdd20627c47425adc760500f672132fd3128e0e7853565611ffeff99f04c7d8b2"
         },
         "path" : "hmat/hamatac.coma",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/hamatac.coma"
      },
      {
         "fixity" : {
            "sha512" : "f280aa5ec5f07f511770f76c2360899cf13a90aca2d4bb690ac84131b29bfaf8ac9bcc653332ecd23fa13b1086019f5b6110ef0ae93e3ee1dbc751bd920dc68b"
         },
         "path" : "hmat/corpus-materials/MapTask_1b.png",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1b.png"
      },
      {
         "fixity" : {
            "sha512" : "8a5e73d767d4e344426ea4112ab3f5f0fdac5d587f6d32defe3497442b082ce25e75f29c562050d73a312318ab335db545afe3e93e85a98ec14a407ac55d5da4"
         },
         "path" : "hmat/corpus-materials/MapTask_1b.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1b.pdf"
      },
      {
         "fixity" : {
            "sha512" : "2f242315eeea8e66940cdca134cbc9c1b24a7468991b91eeb64efb4b8f487ebc7d6c604befe399773d948d30fd73b6892f25806d20e1fd410e067676f79df2c3"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.mp3",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.mp3"
      },
      {
         "fixity" : {
            "sha512" : "eaeaffdc39f13c7d9887853e8f3e855bf468f6b1d9a893140d922cd9086822c64ff6f21cf18765bef0090054e8fae0a6fc4e2f9d8a892a36a6a4f37c40fda576"
         },
         "path" : "hmat/corpus-materials/MapTask_2a.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2a.pdf"
      },
      {
         "fixity" : {
            "sha512" : "f0a12971d23cdbf7ffa55c83a715f62c77da5489cca0749b426ceb619ae95f74ce0cd2b8b0c12a6923e6035f301583833ff0708c8fab3cea9d27e978301e446c"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.wav",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.wav"
      },
      {
         "fixity" : {
            "sha512" : "0bb13a7d95f6ca073cc3437f656883316c859534d9a6315585122e08950b2bf995c8c424cfc16e8b3c3c11af91648827049f952bbb22068d7624f604b38fdc3a"
         },
         "path" : "hmat.zip",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content/hmat.zip"
      },
      {
         "fixity" : {
            "sha512" : "19286c51eb27987d85b057cbae8485519b6d0f70fec9c663bc03086e017a91882869d9c4e5f2c85b4f291deb97a97c1d7a815063d5a5b4471bbff5dd20466d48"
         },
         "path" : "hmat/corpus-materials/MapTask_1a.png",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1a.png"
      },
      {
         "fixity" : {
            "sha512" : "7f96117a77948c21ab4d2e1ad0450382a445ad692251c0606abe059b083058e278ac43c2b83bbc7283da969a338f8a03ed20b8fd7b2f44cffd71c594b8d588c4"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.mp3",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.mp3"
      },
      {
         "fixity" : {
            "sha512" : "c08cca924f2780abe61184d5d4f2548d7333aeb8aafaee7fd815814d5b937e654082db4414421fcc0ca71bde2de8991dac4f09dd1c73290c68ddd06584b4fc3c"
         },
         "path" : "hmat/documentation/Disfluency_Annotation_Guidelines.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/documentation/Disfluency_Annotation_Guidelines.pdf"
      },
      {
         "fixity" : {
            "sha512" : "405ea3bd0643f4314f5d9658e2442f692ec9a521ecfc625af4b5f44198e6798f1b16636c701c55e0a7a49e237eb6348235989248019c259a8945db329f6792c7"
         },
         "path" : "hmat/corpus-utilities/annotation-specification-disfluency.xml",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/annotation-specification-disfluency.xml"
      },
      {
         "fixity" : {
            "sha512" : "982b2a0f8b3b2b5eb3de4d1430f5b89c152f9dcc61255860bfe98f06fa38d39ee34b985fd29fdd9cd7e1638c3bd464f3fa2dc25dd9bf400b506661e85d911b81"
         },
         "path" : "hmat/corpus-utilities/FormatTable4BasicTranscription.xsl",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/FormatTable4BasicTranscription.xsl"
      },
      {
         "fixity" : {
            "sha512" : "5cc9ad194669a5fe86ca51ed870c3e2e6bd4864c8f0248fb06e078b156c6cd68151fd769ac286d732e2347cf9456f1b6ca048ae51bf369c3608b6fb85ab95a7d"
         },
         "path" : "hmat/corpus-materials/MapTask_2b.pdf",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2b.pdf"
      },
      {
         "fixity" : {
            "sha512" : "6707a9d0aa64b53b612c8ecfbbdab55972d88b974a0e1612f2bf6d8acfe84da1d494881511b737ab2d2629ad39a41bc70987f13e2a2fc599aa7059a1f596fe1a"
         },
         "path" : "hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali_s.exs",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali_s.exs"
      },
      {
         "fixity" : {
            "sha512" : "5348b651f9c6bf036770c7afbc8a1a8bd3ea7efd7323c7a19e66f2ab23875785ce1438cc067ee079de85109544cfaa8eb16aa33c7469ebcd1662a0e401879667"
         },
         "path" : "hmat/Ali_Dimitri.zip",
         "storageRelativePath" : "e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri.zip"
      }
   ],
   "mutable" : false,
   "objectId" : "ids:hmat",
   "objectVersionId" : {
      "objectId" : "ids:hmat",
      "versionNum" : "v2"
   },
   "versionInfo" : {
      "created" : 1652974796.63851,
      "message" : "Update of hmat",
      "user" : {
         "address" : "mailto:lange@ids-mannheim.de",
         "name" : "Herbert Lange"
      }
   },
   "versionNum" : "v2"
}

% find ocfl-repo
ocfl-repo
ocfl-repo/0004-hashed-n-tuple-storage-layout.md
ocfl-repo/extensions
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout
ocfl-repo/extensions/0004-hashed-n-tuple-storage-layout/config.json
ocfl-repo/ocfl_1.0.txt
ocfl-repo/e3
ocfl-repo/e3/63
ocfl-repo/e3/63/f2
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/inventory.json
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri.zip
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/FormatTable4BasicTranscription.xsl
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/annotation-specification-disfluency.xml
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-utilities/AnnotationPanel_STTS_folk.xml
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2b.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1a.png
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1b.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2b.png
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2a.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_2a.png
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1a.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/corpus-materials/MapTask_1b.png
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/hamatac.coma
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/documentation
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/documentation/HAMATAC.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/documentation/Disfluency_Annotation_Guidelines.pdf
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri_s.exs
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.BMP
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.mp3
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.exb
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Dimitri/MT_091209_Dimitri.wav
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.wav
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.mp3
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali_s.exs
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.BMP
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/content/hmat/Ali_Dimitri/MT_091209_Ali/MT_091209_Ali.exb
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v2/inventory.json.sha512
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/0=ocfl_object_1.0
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/inventory.json
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/content/hmat.zip
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/v1/inventory.json.sha512
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/inventory.json
ocfl-repo/e3/63/f2/e363f241d949abde8b4641da4f44bc45af58bfa257ae3d5f1010eb7ac034ce4ad123e2fc98c4d6e04aca0a1e11911f4a2142580ac9d48982595c4802f1194e42/inventory.json.sha512
ocfl-repo/ocfl_extensions_1.0.md
ocfl-repo/ocfl_layout.json
ocfl-repo/0=ocfl_1.0
```

As a result we see that a new version has been created and the extracted files have been added to the content of this version. Since the original zip file has not changed,
it is still only stored in the `v1/content` folder and the inventory of version 2 points to this file in the previous version.