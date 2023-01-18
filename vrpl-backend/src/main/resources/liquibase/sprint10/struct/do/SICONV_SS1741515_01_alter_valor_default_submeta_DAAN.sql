-- Mudança do valor padrão do campo de ELA - Em Elaboração para EPE - Em Preenchimento

ALTER TABLE siconv.vrpl_submeta ALTER COLUMN in_situacao SET DEFAULT 'EPE'::character varying;
