
# FieldAgentFullStack

### Security Clearance

* [ ] GET all security clearances
* [ ] GET a security clearance by ID
* [ ] For GET return a 404 if security clearance is not found
* [ ] POST a security clearance
* [ ] For POST return a 400 if the security clearance fails one of the domain rules
  * [ ] Security clearance name is required
  * [ ] Name cannot be duplicated
* [ ] PUT an existing security clearance
* [ ] For PUT return a 400 if the security clearance fails one of the domain rules
* [ ] DELETE a security clearance that is not in use by ID
* [ ] For DELETE return a 404 if the security clearance is not found
* [ ] For DELETE return a 400 if the security clearance is in use 

### Alias

* [ ] GET an agent record with aliases attached
* [ ] POST an alias
* [ ] For POST return a 400 if the alias fails one of the domain rules
  * [ ] Name is required
  * [ ] Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
* [ ] PUT an alias
* [ ] For PUT return a 400 if the alias fails one of the domain rules
* [ ] DELETE an alias by ID
* [ ] For DELETE Return a 404 if the alias is not found

### Global Error Handling

* [ ] Return a specific data integrity error message for data integrity issues
* [ ] Return a general error message for issues other than data integrity


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) throws Exception {
        // log this error...

        if (ex instanceof HttpMessageNotReadableException || ex instanceof HttpMediaTypeNotSupportedException) {
            throw ex; // return
        }
