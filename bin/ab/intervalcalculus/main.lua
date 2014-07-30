function GetRA (object1_start, object2_start, object1_centre, object2_centre, object1_end, object2_end)
  local rel = "NULL"
  -- Relation Takes Place Before
  if (object1_start < object2_start and object1_end < object2_start) then 
  rel = "TAKES_PLACE_BEFORE"
  elseif (object2_start < object1_start and object2_end < object1_start) then 
  rel = "INVERSE_TAKES_PLACE_BEFORE"

  -- Relation Meets
  elseif(object1_end == object2_start and object1_start < object2_start) then
  
  rel = "MEETS"
  
  elseif (object2_end == object1_start and object2_start < object1_start) then
  
  rel = "INVERSE_MEETS"
  

  -- Relation OVERLAPS
  elseif(object1_start < object2_start and object2_start < object1_end and object1_end < object2_end) then
  
  rel = "OVERLAPS"
  
  elseif(object2_start < object1_start and object1_start < object2_end and object2_end < object1_end) then
  
  rel = "INVERSE_OVERLAPS"
  

  -- Relation STARTS
  elseif(object1_start == object2_start and object1_end < object2_end and object2_start < object1_end) then
  
  rel = "STARTS"
  
  elseif(object1_start == object2_start and object2_end < object1_end and object1_start < object2_end) then
  
  rel = "INVERSE_STARTS"
  

  -- Relation DURING
  elseif(object1_start > object2_start and object1_end < object2_end and object1_start < object2_end and object1_end > object2_start) then
  
  rel = "DURING"
  
  elseif(object2_start > object1_start and object2_end < object1_end and object2_start < object1_end and object2_end > object1_start) then
  
  rel = "INVERSE_DURING"
  

  -- Relation FINISHES
  elseif(object1_end == object2_end and object2_start < object1_start and object1_start < object2_end) then
  
  rel = "FINISHES"
  
  elseif(object1_end == object2_end and object1_start < object2_start and object2_start < object1_end) then
  
  rel = "INVERSE_FINISHES"
  

  -- Relation EQUAL
  elseif(object1_start == object2_start and object1_end == object2_end) then
  
  rel = "EQUAL"
  end
  return rel
end