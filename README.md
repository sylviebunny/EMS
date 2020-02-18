# Enfec-EMS

## Event Service

### ../event/search?event_id=1       
       Search event by event_id, this should be only accessed by admin.  

### ../event/search/{anything}
       Search events by anything exclude id information, this should be used for filtered bar. 
       
### ../event/search/by_date?start_date=2020-07-01&end_date=2020-08-01
       Search events within a date range

### ../event/create
       Create an event by organizer or admin

### ../event/update
       Update an event by organizer or admin 
       
### ../event/delete/{Event_ID}
       Delete an event by organizer or admin
