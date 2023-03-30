*** Settings ***
Library           Collections
Library           RequestsLibrary
Test Timeout      30 seconds

*** Test Cases ***
addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=George Orwell    actorId=nm31  
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=Devin
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400

addMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=Titanic    movieId=n76m1  imdbRatings=${7.8}  runtime=${120}
    ${resp}=    Put Request    localhost    /api/v1/addMovie    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addMovieFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=Titanic   imdbRatings=7.8  runtime=120
    ${resp}=    Put Request    localhost    /api/v1/addMovie    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400


addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=nm31    movieId=n76m1
    ${resp}=    Put Request    localhost    /api/v1/addRelationship    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200


addRelationshipFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=ghjkkjh31    movieId=n76m1
    ${resp}=    Put Request    localhost    /api/v1/addRelationship    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

GetMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     movieId=n76m1   
    ${resp}=    Get Request    localhost    /api/v1/getMovie    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

GetMovieFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=titanic   
    ${resp}=    Get Request    localhost    /api/v1/getMovie    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400

GetMovieRuntimePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     runtime=${100}   
    ${resp}=    Get Request    localhost    /api/v1/getMovieByRuntime    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

GetMovieRuntimeFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     id=100   
    ${resp}=    Get Request    localhost    /api/v1/getMovieByRuntime    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400


GetMovieRatingsPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     imdbRatings=${5.0}    
    ${resp}=    Get Request    localhost    /api/v1/getMovieByRatings    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

GetMovieRatingsFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=titanic   
    ${resp}=    Get Request    localhost    /api/v1/getMovieByRatings    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400


GetActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm31   
    ${resp}=    Get Request    localhost    /api/v1/getActor    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

GetActorFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=George   
    ${resp}=    Get Request    localhost    /api/v1/getActor    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400


GetActorByAgePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     age=${20}   
    ${resp}=    Get Request    localhost    /api/v1/getActorByAge    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

GetActorByAgeFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=George   
    ${resp}=    Get Request    localhost    /api/v1/getActorByAge    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400

hasRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm31   movieId=n76m1  
    ${resp}=    Get Request    localhost    /api/v1/hasRelationship   json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

hasRelationshipFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm6731   movieId=n76m1  
    ${resp}=    Get Request    localhost    /api/v1/hasRelationship   json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404


addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    name=Kevin Bacon    actorId=nm0000102  age=${49}
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=nm0000102    movieId=n76m1
    ${resp}=    Put Request    localhost    /api/v1/addRelationship    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200


BaconPathPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=nm31   
    ${resp}=    Get Request    localhost    /api/v1/computeBaconPath    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

BaconPathFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=n55m31   
    ${resp}=    Get Request    localhost    /api/v1/computeBaconPath    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404


BaconNUmberPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=nm31   
    ${resp}=    Get Request    localhost    /api/v1/computeBaconNumber    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

BaconNUmberFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary    actorId=n55m31   
    ${resp}=    Get Request    localhost    /api/v1/computeBaconNumber    json=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404