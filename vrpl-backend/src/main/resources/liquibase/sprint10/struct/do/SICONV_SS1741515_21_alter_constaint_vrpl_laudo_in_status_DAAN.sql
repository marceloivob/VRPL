ALTER TABLE siconv.vrpl_laudo DROP CONSTRAINT vrpl_laudo_in_status;

ALTER TABLE siconv.vrpl_laudo ADD CONSTRAINT vrpl_laudo_in_status CHECK ((in_status = ANY (ARRAY[(1)::int8, (2)::int8, (3)::int8, (4)::int8])));