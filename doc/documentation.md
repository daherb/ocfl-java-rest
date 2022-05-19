# Documentation

This software provides a simple REST API to access a OCFL store. It provides four operations

## List objects

URL: `/list_objects`

Parameters: none

Result: JSON list of strings containing all object identifiers

## Validate objects

URL: `/validate_objects`

Parameters: none

Result: JSON object mapping from object identifiers to validation results

## Put object

URL: `/put_object`

Parameters:
- `object_id`: The ID of the object. If the object does not exist, it will be created, otherwise it will be updated
- `path`: Path to the folder containing the files the object should contain
- `name`: The name of the user
- `address`: The contact of the user, should be an URI, e.g. mailto:
- `message`: A short string similar to a Git commit message

Result: JSON object representing the most recent state of the object, i.e. after putting it into the store

## Get object

URL: `/get_object`

Parameters:
- `object_id`: The ID of the object. If the object does not exist it creates an error
- `path`: The path where the files stored in the object should be copied. The target itself must not exist but its parent must exist

Result: JSON object representing the current state of the object
