package br.gov.planejamento.siconv.mandatarias.test.core;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;

public class DataFactory {

	public static final String CONCEDENTE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MzQ0Mzc0LCJpZFByb3Bvc3RhIjo3NzMwMDYsInJvbGVzIjpbIkdFU1RPUl9DT05WRU5JT19DT05DRURFTlRFIl0sImlzcyI6InNpY29udmlkcCIsImF1ZCI6IlZSUExEIiwiY3BmIjoiMjQwMDE3NzUxMTUiLCJub21lIjoiSk9TRSBBTlRPTklPIERFIEFHVUlBUiBORVRPIiwiaWF0IjoxNTQ1MTM2Njk0LCJlbnRlIjoiMDYxODQyNTMwMDAxNDkiLCJ0aXBvRW50ZSI6IkNPTkNFREVOVEUifQ.J8NdmItr7wjZ1FepJ5Ol6Ty0uCO4ST-Iqq7MdgiN1vTHfH0UkaSY4Ki_zkHTvmP4AvyRsd9AA871YvDHD8SMC3FpktE2-gl0jTn6KdzQwEM8IIIHwYXtmPPAN1DWR-L_hP97m2jaKZ-aFP1UHedTKEyoaQTEf2oRFjyN40wgqI7kXxA5fF2_lrCDzr9SXn4DAAZLXShdZ-jXWDPFHI-LSyRKnBhtpBCB4QRjupaxR6diI-iOgF7CvE3bDNt96uus_s5EdM2KMkNwt4ZcasNcPIWdO6QLtO4RKMkW0KI4WCMN4_JcCIogEp0l1twgqcVC23tiT_2MZKPy2sCBIx9BQQ";

	public static final String MANDATARIA = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MzQ0Mzc0LCJpZFByb3Bvc3RhIjo3NzMwMDYsInJvbGVzIjpbIkdFU1RPUl9DT05WRU5JT19DT05DRURFTlRFIl0sImlzcyI6InNpY29udmlkcCIsImF1ZCI6IlZSUExEIiwiY3BmIjoiMjQwMDE3NzUxMTUiLCJub21lIjoiSk9TRSBBTlRPTklPIERFIEFHVUlBUiBORVRPIiwiaWF0IjoxNTQ1MTM2NzU1LCJlbnRlIjoiMDYxODQyNTMwMDAxNDkiLCJ0aXBvRW50ZSI6Ik1BTkRBVEFSSUEifQ.bi6wA1kLKvZEKYtvZs8gSyvwvZHk8888M1vcqyKeuVyQ2FlhcXSvVlspPLz0OxnHSGafyWTqJQwoUuPtst90JAqqV4Ozs1jjPlYo1lF5JCccMBQzFOWZqh68VUD8kWbvqQDb1d297v5trSxXc0l4LRP6reeCRYdk62EGuAxoUl1Av__ySH-l_TCwCzXRPnUNwT8QZlKRSjFPtkJBvssInAa7DzDGGo-Sfa-LfAfriWS8yqbPPdgLplDqwFLM1MlWn05EkExPxxAgvwWKs7IkjJE_0Kr1A8AFjBeZ1HzOzO5x6lkx6-P8DSr2eToUuv7-mqETdGoMljcUb9OsY0Kl8w";

	public static final String PROPONENTE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MzQ0Mzc0LCJpZFByb3Bvc3RhIjo3NzMwMDYsInJvbGVzIjpbIkdFU1RPUl9DT05WRU5JT19DT05WRU5FTlRFIl0sImlzcyI6InNpY29udmlkcCIsImF1ZCI6IlZSUExEIiwiY3BmIjoiMjQwMDE3NzUxMTUiLCJub21lIjoiSk9TRSBBTlRPTklPIERFIEFHVUlBUiBORVRPIiwiaWF0IjoxNTQ1MTM2NDM1LCJlbnRlIjoiMDYxODQyNTMwMDAxNDkiLCJ0aXBvRW50ZSI6IlBST1BPTkVOVEUifQ.FKDX2LQGzPo0WYAXSdBzbqcxmQAd4wILCAuvW_Rk0W8Z5VJuNApnL4NLuugbvFT39gKaeVM3gvQ83NMRuWNj8qx7XbLalgT_wbvczCjpImvU5rN1nzGdhXqyKJKqbKj-QBH4J8mBC1EEETyZEtr8qRe9TIGRBKD1qaeLTGbp9Tp48utjGpFqhXcS3ZctNMO-ZXv6LkW8UnZQhL_e1oNmE8d7X9quKWKOyi7ia6ls8wK3HgiIAYRc-t14jD6xLQsF0DuUTMHZ6WCY4XFsPY1g5kOyZMRfdF-vZBbziPbe0mSkhhGX_aWfo3h8hhYSpbLaacFenJ0mpdz1MvEou5gXsQ";

	public static final String GUEST = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRQcm9wb3N0YSI6MTMzMjA1MiwiaXNzIjoic2ljb252aWRwIiwiYXVkIjoiVlJQTEQiLCJjcGYiOiJndWVzdCIsIm5vbWUiOiJHdWVzdCIsImlhdCI6MTU1Mzg3ODY0NX0.dJkaFzurY7knd9K-Fbm9s_nqUqOY-MsYqKwCl5cv7qazLLJLVO5_4RzTUq-Y5bzK4qZCze8C25eCgRo3EaHe_IMB1VM-vTyzC4CriCzW0jG5V_PNEktmS4o-Jj3Yhhfhz7ZSLB_k0bdER2zf5FQHHIhBmPFx1AoyXTMputUvOkjF8rw18zbD24UCVa9R0cNVLsBnvFhIkns2WzLA17gK8FeaVPZZw9hU59Cl-on6jnVNf98KEqx3Sbo1n-4ONdwmE--QjS-zeV1s1bvZW2aGQ8ERpXgmrb1QSmtXdrsDLAIqdzU2T3uy5Svtu9NHg3BYTfGu06YLuyAxiYQiZkwv-A";

	/**
	 * Token com o Identificador da Proposta inválido, para testar os retornos
	 * vazios das listagens e exibir o resultado "Nenhum registro encontrado."
	 */
	public static final String TOKEN_COM_ID_PROPOSTA_INVALIDA = "";

	/**
	 * Token Inválido
	 * <p>
	 * { "alg": "RS256", "typ": "JWT" }
	 * </p>
	 * <p>
	 * { "sub": "1234567890", "name": "John Doe", "admin": true, "iat": 1516239022 }
	 * </p>
	 */
	public final static String TOKEN_JWT_ASSINATURA_INVALIDA = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.TCYt5XsITJX1CxPCT8yAV-TVkIEq_PbChOMqsLfRoPsnsgw5WEuts01mq-pQy7UJiN5mgRxD-WUcX16dUEMGlv50aqzpqh4Qktb3rk-BuQy72IFLOqV0G_zS245-kronKb78cPN25DGlcTwLtjPAYuNzVBAh4vGHSrQyHUdBBPM";

	public final static String TOKEN_JWT_INVALIDO = "";

	/**
	 * Token expirado em: 19/09/2018 10:05
	 * <p>
	 * { "alg": "RS256", "typ": "JWT" }
	 * </p>
	 * <p>
	 * { "id": 1247, "idProposta": 773006, "exp": 1537362300, "iss": "siconvidp",
	 * "aud": "VRPL", "cpf": "24001775115", "nome": "JOSE ANTONIO DE AGUIAR NETO",
	 * "iat": 1537360500 }
	 * </p>
	 ***/
	public final static String TOKEN_JWT_EXPIRADO = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTI0NywiaWRQcm9wb3N0YSI6NzczMDA2LCJleHAiOjE1MzczNjIzMDAsImlzcyI6InNpY29udmlkcCIsImF1ZCI6IlZSUEwiLCJjcGYiOiIyNDAwMTc3NTExNSIsIm5vbWUiOiJKT1NFIEFOVE9OSU8gREUgQUdVSUFSIE5FVE8iLCJpYXQiOjE1MzczNjA1MDB9.HKz1uExkurtgahuI3gKaSDW4e68-Gqzj-mN7hhISZCKdzxXIg7ZLQJcXHztjn8QivPKXe3PbgB-xzndUPdCna_GRXj5KO3Xwt_S0Kwj1BaVrF-VVGg7_vPTylnNKocY94G-m6Yc_y-ofYS1O7KYrOM1PPnRAIWgAu-9H30PxTqqAhmhxsSUa84m307DZ1_zF4m6F1S-18kd96CUvpC4lfhHwoVJ12nCbmpWtRtE3pBk00Y4NBH7CtlASQ-4LtivTn91uY5sGbRjrb5OFMJ3KP5gJfbw4Z0KwTFe7B1fqaaqwQ82zogDc_mQKbUCOz4_nx-exhfd-ILi4Z7ZaRQgNbA";

	public static final String INSERT_PROPOSTA = "INSERT INTO siconv.vrpl_proposta (id_siconv,numero_proposta,ano_proposta,valor_global,valor_repasse,valor_contrapartida,modalidade,nome_objeto,numero_convenio,ano_convenio,data_assinatura_convenio,codigo_programa,nome_programa,identificacao_proponente,nome_proponente,uf,pc_min_contrapartida,nome_mandataria,categoria,nivel_contrato,spa_homologado,apelido_empreendimento,adt_login,adt_data_hora,adt_operacao,versao,versao_id,versao_nm_evento,versao_nr,versao_in_atual) VALUES "
			+ " (1,17953,10154,-1204208640.49,-83248112.61,1.00,1,'dolor in reprehenderit in voluptate velit esse cillum dolore eu ',9515,28064,'2005-12-16','mo','magna a','1','adipiscing elit, sed do eiusmod tempor inci','s',1.00,'exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in r','sint occaecat cupidatat non proiden','Excepteur sint ',true,'apelido','SISTEMA','1997-04-17 07:09:28.377','INSERT',25678,'1','d',1,true), "
			+ " (3,8693,28704,-1154140544.64,-598718144.34,3.00,3,'amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ',20467,25431,'1984-08-29','in volup','occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id es','3','incididunt ut labore et dolore magna aliqua. U','D',3.00,'cupidatat non proident, sunt in culpa','deserunt mo','qui offici',false,'apelido','SISTEMA','1992-09-05 17:06:53.183','INSERT',9692,'3','de',10895,false), "
			+ " (4,23347,29908,-521190368.42,-1321017984.94,4.00,4,'mollit anim id est labo',17961,14532,'1998-01-22','Ut enim ad ','non proident, sunt in culpa qui offici','4','et dol','q',4.00,'proident, sunt in culpa qui officia deserunt m','Du','irure dolor in repr',true,'apelido','SISTEMA','1996-09-01 18:59:58.560','INSERT',7494,'4','d',32649,true), "
			+ " (5,3153,23832,-81764984.17,-114532768.67,5.00,5,'dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt i',19702,23298,'1983-01-25','elit, s','Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ','5','non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem i','u',5.00,'ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum ','sint occaecat cupidatat non proide','ip',true,'apelido','SISTEMA','2014-01-10 07:34:04.302','INSERT',7342,'5','d',32309,true), "
			+ " (7,30534,19841,-1282594944.73,-242721584.89,7.00,7,'sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, conse',18850,31176,'2016-12-24','consectetur','sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laboru','7','Ut eni','d',7.00,'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariat','qui officia deserunt mollit anim id est la','sed do eiusmo',false,'apelido','SISTEMA','1986-11-14 14:47:48.464','INSERT',4946,'7','ni',19756,false), "
			+ " (8,7642,6717,-305252416.24,-1378108416.81,8.00,8,'fugiat nulla pariatur. Excepteur sint occaecat ',7035,6359,'2016-12-23','qui offici','qui officia deserunt mollit anim id est','8','ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ulla','e',8.00,'deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur ad','minim veniam, quis nostrud exe','in voluptat',true,'apelido','SISTEMA','2016-01-29 12:24:45.355','INSERT',24161,'8','a',28223,false), "
			+ " (1,4978,548,-205250080.34,-1279897472.99,2.00,2,'non proident, sunt in culpa qui officia deserunt m',28719,20233,'2011-02-21','ullamc','sunt in culpa qui officia deserunt mo','2','ad','mo',2.00,'pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deseru','exercitation ullamco ','tempor',true,'apelido','SISTEMA','1980-03-08 21:56:40.082','INSERT',29053,'2','lab',7296,false), "
			+ " (1,19234,29689,-781266048.25,-1498415872.26,6.00,6,'in culpa qui officia deserunt mollit anim',9812,19062,'1980-10-13','magna','cupidatat non proident, sunt in culpa qui officia deserunt mollit anim i','6','ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in rep','e',6.00,'ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labo','dolore magna aliqua. Ut enim ad minim veniam','ullamco laboris',false,'apelido','SISTEMA','1980-12-21 08:40:22.422','INSERT',20935,'6','qui',24266,false)";

	public static final String INSERT_LICITACAO = "INSERT INTO siconv.vrpl_licitacao (proposta_fk,numero_ano,objeto,valor_processo,status_processo,id_licitacao_fk,adt_login,adt_data_hora,adt_operacao,in_situacao,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ "(5,'nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute ir','dolore eu fugiat nulla pariatur. Excepteur sint occae',-626332416.34,'do eiusm',1,'SISTEMA','2017-02-22 01:11:06.476','INSERT','VI',4777,'1','et',0)\n"
			+ ",(3,'dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullam','ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip',-934057792.66,'dolor in rep',2,'SISTEMA','1996-08-27 00:57:24.054','INSERT','es',21436,'2','ad',0)\n"
			+ ",(4,'velit esse cillum dolore eu fugiat nulla pariatur. Exc','voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur s',-1142004736.53,'volupt',3,'SISTEMA','2014-07-02 20:21:37.606','INSERT','e',19833,'3','non',0)\n"
			+ ",(3,'borum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eius','do eius',-1293621376.10,'pariatur.',4,'SISTEMA','1994-05-11 05:49:26.844','INSERT','Dui',22703,'4','Exc',0)\n"
			+ ",(3,'laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusm','cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lo',-1340679424.76,'exercitation ull',5,'SISTEMA','2006-03-14 03:43:28.422','INSERT','u',161,'5','e',0)\n"
			+ ",(1,'in ','elit, sed do eiusmod tempor incididunt ut labore e',-1148434944.42,'cillum dolore eu f',6,'SISTEMA','2001-09-17 20:04:02.726','INSERT','eu',26234,'6','et ',0)\n"
			+ ",(3,'cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum do','consequat. Duis aute irure dolor in rep',-1455160832.71,'quis nostrud ',7,'SISTEMA','1989-04-13 05:33:16.702','INSERT','in ',11189,'7','Dui',0)\n"
			+ ",(8,'ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in rep','laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in volupta',-450894880.30,'ea commodo consequa',8,'SISTEMA','1981-01-05 07:45:30.062','INSERT','u',5346,'8','l',0)\n"
			+ ",(3,'mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmo','sit amet, consectetur adipiscing elit, sed do eiusmod tem',-865414656.33,'sunt in c',9,'SISTEMA','2016-01-06 14:14:26.254','INSERT','s',7254,'9','et ',0)\n"
			+ ",(2,'adipi','in reprehenderit in voluptate velit esse cillum',-1184687744.71,'deserunt mo',10,'SISTEMA','2012-03-19 11:29:09.332','INSERT','p',17439,'10','ad ',0);"
			+ "INSERT INTO siconv.vrpl_licitacao (proposta_fk,numero_ano,objeto,valor_processo,status_processo,id_licitacao_fk,adt_login,adt_data_hora,adt_operacao,in_situacao,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ "(4,'ullamco laboris nisi ut aliquip ex ea ','aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse',-538280960.66,'quis nostrud exer',11,'SISTEMA','1987-08-07 17:40:45.593','INSERT','dol',14367,'11','el',0)\n"
			+ ",(4,'laboris nisi ut aliquip ex ea commodo cons','anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod',-1154075264.23,'exercitation ullamc',12,'SISTEMA','2001-12-06 07:12:14.869','INSERT','con',7848,'12','su',0)\n"
			+ ",(3,'nisi ut aliquip ex ea commodo consequat.','tem',-1210417664.40,'cupidata',13,'SISTEMA','2008-02-19 09:03:48.205','INSERT','c',3709,'13','a',0)\n"
			+ ",(1,'aliqua. Ut enim ad minim veniam, quis nostrud','qui officia deserunt mollit anim id est la',-340205120.52,'et dolore magna',14,'SISTEMA','2016-10-20 13:50:47.982','INSERT','au',17156,'14','ven',0)\n"
			+ ",(2,'sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ','sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidi',-488711680.39,'sunt in culpa qui of',15,'SISTEMA','2016-12-12 10:56:58.232','INSERT','dol',21201,'15','sit',0)";


	public static final String INSERT_LOTE_LICITACAO = "INSERT INTO siconv.vrpl_lote_licitacao (licitacao_fk,numero_lote,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ " ( 6,2621,'LOGIN','1993-07-29 13:20:19.934','INSERT',8394,'1','do',0)\n"
			+ ",(1,6085,'LOGIN','1991-08-22 16:41:15.420','INSERT',28057,'2','a',0)\n"
			+ ",(2,199,'LOGIN','2012-12-14 22:02:09.124','INSERT',631,'3','cu',0)\n"
			+ ",(3,6106,'LOGIN','2007-06-04 20:27:44.170','INSERT',26305,'4','do ',0)\n"
			+ ",(4,29899,'LOGIN','1985-11-07 00:00:25.663','INSERT',12609,'5','u',0)\n"
			+ ",(5,31407,'LOGIN','2016-04-05 08:23:36.181','INSERT',27329,'6','e',0)\n"
			+ ",(6,11345,'LOGIN','1998-04-20 11:31:22.249','INSERT',5770,'7','c',0)\n"
			+ ",(7,17888,'LOGIN','1996-02-04 08:12:41.651','INSERT',6406,'8','ci',0)\n"
			+ ",(8,25385,'LOGIN','1994-08-22 06:55:11.050','INSERT',9325,'9','inc',0)\n"
			+ ",(1,31176,'LOGIN','1996-04-18 06:01:39.644','INSERT',31579,'10','ut ',0);\n"
			+ "INSERT INTO siconv.vrpl_lote_licitacao (licitacao_fk,numero_lote,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ " (2,17866,'LOGIN','1999-05-16 00:23:55.826','INSERT',1733,'11','dol',0)\n"
			+ ",(3,3220,'LOGIN','2003-01-15 15:29:58.591','INSERT',27124,'12','ve',0)\n"
			+ ",(4,1147,'LOGIN','1984-09-19 21:00:27.517','INSERT',15806,'13','el',0)\n"
			+ ",(5,8741,'LOGIN','1993-02-11 18:19:00.373','INSERT',3652,'14','dol',0)\n"
			+ ",(6,9925,'LOGIN ','2008-01-23 11:55:51.544','INSERT',20866,'15','u',0);";

	public static final String INSERT_SUBTIEM_LICITACAO = "INSERT INTO siconv.vrpl_subitem_investimento \n"
			+ "    (id, id_subitem_analise,descricao,in_projeto_social,codigo_und,descricao_und,versao,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento) \n"
			+ "VALUES \n"
			+ "    (4, 1,'quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequ','q','adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. U','cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsu',10455,'LOGIN','1998-01-10 06:08:42.169','INSERT',28752,'1','ame'),\n"
			+ "    (5, 2,'consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu ','ad','u','in voluptate velit esse cillum dolore eu fugiat nulla paria',13250,'LOGIN','2017-03-27 18:07:18.232','INSERT',26939,'2','n'),\n"
			+ "    (6, 3,'ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullam','mag','laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure d','nostrud exercit',27005,'LOGIN','1994-12-11 23:08:45.877','INSERT',9669,'3','ex'),\n"
			+ "    (7, 4,'mollit anim id est laborum.Lorem ips','n','deserunt mollit anim id est laborum.Lorem ipsum dolor sit am','sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consecte',21305,'LOGIN','2017-07-29 03:40:46.031','INSERT',10003,'4','s'),\n"
			+ "    (8, 5,'non proident, sunt in culpa qui officia deserunt mollit anim','non ','adipiscing elit, sed do eiusmod ','ullamc',7812,'LOGIN','1982-11-13 18:20:37.833','INSERT',10131,'5','la'),\n"
			+ "    (9, 6,'laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit i','U','aliquip ex ea commodo consequat','ut labore et dolore magna aliqua',28164,'LOGIN','1991-06-06 09:59:50.768','INSERT',9005,'6','par')";

	public static final String INSERT_META = "INSERT INTO siconv.vrpl_meta (id_meta_analise,tx_descricao_analise,nr_meta_analise,qt_itens_analise,in_social,adt_login,adt_data_hora,adt_operacao,subitem_fk,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ "(1,'nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. D',9894,-934949824.45,false,'LOGIN','1982-03-15 22:45:59.541','INSERT',4,1194,'1','sun',7838)\n"
			+ ",(2,'ut aliquip ex ea commodo consequat. Duis aute irure ',9922,-1482727808.14,false,'LOGIN','1986-07-21 23:34:44.726','INSERT',9,26618,'2','Dui',10686)\n"
			+ ",(3,'incididunt ut labo',31372,-41260708.71,true,'LOGIN','2013-04-21 17:30:27.765','INSERT',6,3742,'3','ut',3907)\n"
			+ ",(4,'pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt m',6161,-384572672.49,true,'LOGIN','1992-12-30 04:34:32.372','INSERT',5,12979,'4','ad',15459)\n"
			+ ",(5,'tempor incididunt ut labor',25100,-1258252800.43,false,'LOGIN','2007-05-04 11:24:44.790','INSERT',6,7371,'5','cup',24664)\n"
			+ ",(6,'qui offici',16315,-179006032.40,false,'LOGIN','1991-05-20 09:25:36.151','INSERT',8,13408,'6','do',25421)\n"
			+ ",(7,'dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proiden',26456,-1505124992.97,true,'LOGIN','2009-09-03 00:15:57.891','INSERT',9,13347,'7','qu',32229)\n"
			+ ",(8,'tempor incididunt ut labore et dolore magna al',4408,-968536512.70,true,'LOGIN','2016-08-16 21:04:09.311','INSERT',6,20250,'8','ess',28391)\n"
			+ ",(9,'tempor incididunt ',31193,-1469271552.45,false,'LOGIN','1986-06-12 16:50:33.060','INSERT',6,26372,'9','ips',6718)\n"
			+ ",(10,'ullamco',28358,-849492928.24,false,'LOGIN','1999-12-17 16:55:05.406','INSERT',6,6318,'10','dol',495);\n"
			+ "INSERT INTO siconv.vrpl_meta (id_meta_analise,tx_descricao_analise,nr_meta_analise,qt_itens_analise,in_social,adt_login,adt_data_hora,adt_operacao,subitem_fk,versao_nr,versao_id,versao_nm_evento,versao) VALUES \n"
			+ "(11,'consequat. Du',30652,-1102443264.45,false,'LOGIN','2010-09-21 08:39:33.128','INSERT',7,7276,'11','D',1548)\n"
			+ ",(12,'velit esse cillum',10930,-657670336.94,true,'LOGIN','2012-10-23 21:07:08.372','INSERT',9,18819,'12','eli',2172)\n"
			+ ",(13,'dolor sit amet, consectetur ',17805,-623009344.17,true,'LOGIN','2006-05-05 05:16:27.249','INSERT',6,17990,'13','ut ',4030)\n"
			+ ",(14,'ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in repreh',4271,-599251520.67,false,'LOGIN','2014-11-22 14:22:35.235','INSERT',9,13807,'14','occ',6097)\n"
			+ ",(15,'anim id est laboru',19681,-1148979840.46,true,'LOGIN','2011-11-01 00:03:55.351','INSERT',5,10296,'15','c',26202);";

	public static final String INSERT_SUBMETA = "INSERT INTO siconv.vrpl_submeta (id_submeta_analise,proposta_fk,vrpl_lote_licitacao_fk,meta_fk,vl_repasse,vl_outros,nr_submeta_analise,tx_descricao_analise,nr_lote_analise,vl_repasse_analise,vl_contrapartida_analise,vl_outros_analise,vl_total_analise,in_situacao_analise,in_regime_execucao_analise,natureza_despesa_sub_it_fk_analise,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,versao,in_regime_execucao,vl_contrapartida,vl_total_licitado,in_situacao) VALUES \n"
			+ "(1,2,3,7,-114766944.40,-1296952448.50,3311,'nulla pariatur. ',13727,-407590560.30,1.00,-1432011136.78,-73269784.45,'c','L',31531,'LOGIN','1995-09-07 15:45:45.052','INSERT',2390,'1','sin',31726,'q',1.00,-28082402.53,'q')\n"
			+ ",(2,4,3,1,-376029568.94,-306380576.98,8755,'culpa ',9585,-1113684224.73,2.00,-174387616.12,-1064309056.73,'ex','et ',26767,'LOGIN','1982-04-20 13:48:01.531','INSERT',12459,'2','u',16423,'n',2.00,-884837568.44,'q')\n"
			+ ",(3,2,5,8,-1216685056.65,-641698240.20,31699,'sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',3552,-1178209536.83,3.00,-629009344.89,-534720896.20,'i','mag',20319,'LOGIN','2003-08-10 19:06:01.012','INSERT',29564,'3','in ',14609,'et ',3.00,-1273546496.55,'a')\n"
			+ ",(4,6,3,13,-1509559808.78,-1089219712.53,413,'nisi ut aliquip ex ea commodo cons',25623,-1170798848.63,4.00,-1136863104.18,-486705184.59,'in ','do',23246,'LOGIN','1981-05-05 19:56:05.521','INSERT',17564,'4','u',22879,'tem',4.00,-113572720.94,'eu')\n"
			+ ",(5,6,4,9,-989285632.73,-747466368.96,6434,'Excepteur sint occaecat cupidat',2606,-266862720.41,5.00,-177199152.20,-735730688.67,'ull','l',24196,'LOGIN','1990-03-23 19:31:10.639','INSERT',13946,'5','ven',29220,'cu',5.00,-795705472.69,'of')\n"
			+ ",(6,3,11,11,-598379328.86,-134918912.82,14385,'sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad m',20380,-1250804096.66,6.00,-1022896448.38,-778449984.53,'ma','dol',20041,'LOGIN','1984-07-02 05:35:06.475','INSERT',25871,'6','off',15420,'d',6.00,-617254144.34,'a')\n"
			+ ",(7,5,6,10,-428244160.10,-1228152320.96,10595,'proident, sunt in culpa',30543,-591848320.83,7.00,-1060035456.94,-353666144.65,'f','ab',22317,'LOGIN','1980-08-31 03:38:48.745','INSERT',24557,'7','a',31036,'no',7.00,-441570080.69,'p')\n"
			+ ",(8,2,5,10,-35282212.50,-1018763776.32,5807,'irure dolor in reprehenderit in volupta',4710,-1330203648.49,8.00,-640211968.29,-352924736.80,'ul','al',8978,'LOGIN','2009-05-09 03:03:01.151','INSERT',18275,'8','la',11574,'in ',8.00,-50304808.68,'mo')\n"
			+ ",(9,5,11,7,-1132035072.33,-758814848.51,20599,'consequat. Duis aute irure dolor in reprehenderit in volu',32158,-515556512.89,9.00,-1031185856.31,-79718448.11,'s','ani',20544,'LOGIN','1991-03-16 17:09:20.030','INSERT',15101,'9','qui',965,'ni',9.00,-967520064.70,'pr')\n"
			+ ",(10,5,7,13,-664498752.45,-1212765056.76,5913,'sint occaecat cupidatat ',1187,-166576144.32,10.00,-974756288.27,-1283453952.80,'a','nos',19111,'LOGIN','2019-06-22 22:52:15.376','INSERT',13176,'10','exe',32511,'ali',10.00,-1279939712.00,'adi');\n"
			+ "INSERT INTO siconv.vrpl_submeta (id_submeta_analise,proposta_fk,vrpl_lote_licitacao_fk,meta_fk,vl_repasse,vl_outros,nr_submeta_analise,tx_descricao_analise,nr_lote_analise,vl_repasse_analise,vl_contrapartida_analise,vl_outros_analise,vl_total_analise,in_situacao_analise,in_regime_execucao_analise,natureza_despesa_sub_it_fk_analise,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,versao,in_regime_execucao,vl_contrapartida,vl_total_licitado,in_situacao) VALUES \n"
			+ "(11,8,10,4,-899930496.16,-1151223040.29,13963,'sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut lab',13784,-1232708992.20,11.00,-151369360.99,-868476992.73,'t','ad',8029,'LOGIN','1998-01-03 03:16:48.383','INSERT',4172,'11','ull',17625,'ab',11.00,-510596544.41,'e')\n"
			+ ",(12,2,11,12,-824812992.10,-243560672.83,17430,'in reprehenderit in voluptate velit e',17037,-647088640.73,12.00,-1021897984.41,-604131520.34,'in ','e',25834,'LOGIN','1985-11-19 02:27:12.165','INSERT',11264,'12','qui',3790,'pa',12.00,-1323135104.17,'off')\n"
			+ ",(13,8,4,13,-1463180160.70,-1204176384.10,17814,'aliqua. Ut enim ad minim veniam, quis nostrud e',26691,-224361728.85,13.00,-1004605056.14,-565564032.74,'c','ut',16700,'LOGIN','2013-08-01 12:02:02.983','INSERT',15280,'13','u',22369,'in ',13.00,-1157645696.67,'e')\n"
			+ ",(14,2,3,4,-751836096.50,-173943440.54,2949,'sed do eius',11195,-670168000.73,14.00,-472399264.97,-1191722112.12,'adi','n',3701,'LOGIN','1985-04-06 00:08:08.107','INSERT',30379,'14','i',30403,'s',14.00,-1286731008.66,'i')\n"
			+ ",(15,1,11,13,-985592768.45,-793167936.30,1588,'sint occaecat cupidatat ',24626,-1365058944.82,15.00,-1324630400.34,-1009457024.55,'l','i',30165,'LOGIN','1986-08-30 07:17:41.096','INSERT',14525,'15','n',21609,'eu ',15.00,-1330467840.52,'u');";

	public static final String INSERT_ANEXO = "INSERT INTO siconv.vrpl_anexo (vrpl_licitacao_fk,nm_arquivo,caminho,dt_upload,tx_descricao,in_tipoanexo,id_cpf_enviadopor,nome_enviadopor,in_perfil,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,bucket,versao) VALUES \n"
			+ " (1,'qui officia deserunt mollit anim id est laborum','laboris nisi ut aliquip ex ea commodo consequat. Duis au','1983-04-12','Excepteur sint occaecat cupid','OUTROS','1','adipiscing elit, sed do eiusm','PROPONENTE','LOGIN','1989-06-15 16:58:25.935','INSERT',13083,'1','aut','irur',25870)\n"
			+ ",(1,'v','adipiscing elit, sed do eiusmod tempor incididunt ut labore et do','2006-05-28','ullamco laboris nisi','OUTROS','2','tempor incididunt ut labore et dolore magna aliqua.','PROPONENTE','LOGIN','1988-11-16 08:54:22.284','INSERT',598,'2','adi','ullamco ',409)\n"
			+ ",(2,'laboris n','ad minim veniam, quis nostru','1991-04-17','elit, sed do e','OUTROS','3','dolor si','PROPONENTE','LOGIN','2010-03-14 23:55:57.877','INSERT',23089,'3','l','esse cillum dolore ',16503)\n"
			+ ",(2,'dolor in reprehenderit in voluptate ve','nulla pariatur. Excepteur sint occaecat ','1986-06-27','incididunt ut la','OUTROS','4','irure dol','PROPONENTE','LOGIN','2007-02-07 17:31:23.635','INSERT',25952,'4','ad','laboris nisi ut',24274)\n"
			+ ",(1,'eu fugiat','ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit ','1999-09-07','incididunt ut labore ','OUTROS','5','ipsum dolor ','PROPONENTE','LOGIN','2003-08-25 07:15:30.591','INSERT',11416,'5','ex ','do eiusmod temp',6787)\n"
			+ ",(1,'elit, sed do e','esse cillum dolore eu fugiat nulla pariatur. Excepteur s','2001-06-12','commodo co','OUTROS','6','aliqua. Ut enim ad minim veniam, qu','PROPONENTE','LOGIN','1981-09-30 22:43:39.467','INSERT',7515,'6','n','non',8956)\n"
			+ ",(2,'mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do','non proiden','2009-02-25','ipsum dolor','OUTROS','7','ad minim veniam, quis nostru','PROPONENTE','LOGIN','1984-08-10 04:17:07.441','INSERT',7841,'7','bor','Excepteur sint occaecat ',22804)\n"
			+ ",(2,'veniam, quis nostrud ','sint','1985-05-02','irure dolor in reprehen','OUTROS','8','ut labore et dolore magna aliqua. Ut enim ad minim','PROPONENTE','LOGIN','1996-08-20 23:39:33.866','INSERT',9008,'8','q','in',18083)\n"
			+ ",(1,'velit esse cillum dolore eu fugiat nulla pariatur. Exce','nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt m','1999-11-21','cillum do','OUTROS','9','exercitation ullamco laboris nisi ut ','PROPONENTE','LOGIN','2018-09-19 21:13:06.005','INSERT',10553,'9','d','labo',26635)\n"
			+ ",(1,'e','cupidatat non proident, sunt in culpa qui officia deserunt molli','1987-05-13','Ut enim ad minim veniam, quis','OUTROS','10','in culpa qui officia deserunt ','PROPONENTE','LOGIN','2003-04-07 00:26:05.249','INSERT',27591,'10','tem','ullamco laboris nisi ut a',22997);\n"
			+ "INSERT INTO siconv.vrpl_anexo (vrpl_licitacao_fk,nm_arquivo,caminho,dt_upload,tx_descricao,in_tipoanexo,id_cpf_enviadopor,nome_enviadopor,in_perfil,adt_login,adt_data_hora,adt_operacao,versao_nr,versao_id,versao_nm_evento,bucket,versao) VALUES \n"
			+ " (2,'eu fugiat nulla pariatur. Excepteur sint occae','f','1994-08-01','aliquip ex','OUTROS','11','aute irure dolor in reprehender','PROPONENTE','LOGIN','2019-12-20 12:01:34.879','INSERT',1175,'11','D','veniam, quis nostrud exe',12815)\n"
			+ ",(2,'adipiscing','in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur s','1988-01-18','adipiscing elit, sed do eiu','OUTROS','12','commodo consequat.','PROPONENTE','LOGIN','1997-04-10 09:44:02.155','INSERT',20714,'12','a','Ut enim ad minim v',7455)\n"
			+ ",(1,'Duis aute irure dolor in reprehenderit in voluptate v','dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor ','2005-09-07','nisi ut aliquip ex e','OUTROS','13','do eiusmod tempor incididunt ','PROPONENTE','LOGIN','2003-03-03 21:36:01.365','INSERT',3867,'13','eu ','Ut enim ad minim',15393)\n"
			+ ",(2,'dolo','Ut enim ad minim veniam, quis nostrud exercitation ullamco lab','2014-04-23','sint occae','OUTROS','14','ut labore et ','PROPONENTE','LOGIN','2012-02-23 06:48:52.831','INSERT',3104,'14','cu','mollit anim',17931)\n"
			+ ",(1,'labore et dolore m','Excepteur sint occaecat cupidatat non proident','2011-02-01','cillum ','OUTROS','15','amet, consectetur adipis','PROPONENTE','LOGIN','2013-05-30 16:22:03.434','INSERT',25532,'15','n','culpa qui officia deser',20490)\n"
			+ "";

	public static final String INSERT_PO = "INSERT INTO siconv.vrpl_po (id_po_analise, submeta_fk, dt_previsao_inicio_obra, dt_previsao_inicio_obra_analise, qt_meses_duracao_obra, adt_login, adt_data_hora, adt_operacao, in_acompanhamento_eventos, in_desonerado, sg_localidade, dt_base_analise, versao_nr, versao_id, versao_nm_evento, versao ) "
			+ "VALUES(0, currval('siconv.vrpl_submeta_seq'), current_date, current_date, 0, '11111111111', current_timestamp, 'INSERT', false, false, '', '2019-01-01', 0, '', '', 0)";

	public static final String INSERT_MACRO_SERVICO = "INSERT INTO siconv.vrpl_macro_servico ( nr_macro_servico, po_fk, tx_descricao,   versao, adt_login, adt_data_hora, adt_operacao, id_macro_servico_analise, versao_nr, versao_id, versao_nm_evento)"
			+ "values ( 1, 1 , '', 0, '999999999', current_timestamp, 'INSERT', 0, 0, NULL, NULL);"
			+ "INSERT INTO siconv.vrpl_macro_servico ( nr_macro_servico, po_fk, tx_descricao,   versao, adt_login, adt_data_hora, adt_operacao, id_macro_servico_analise, versao_nr, versao_id, versao_nm_evento)"
			+ "values ( 2, 1 , '', 0, '999999999', current_timestamp, 'INSERT', 0, 0, NULL, NULL)";

	public static final String INSERT_SERVICO = "INSERT INTO siconv.vrpl_servico ( macro_servico_fk, nr_servico,  tx_observacao, cd_servico, tx_descricao, sg_unidade, vl_custo_unitario_ref_analise, pc_bdi_analise, qt_total_itens_analise, vl_custo_unitario_analise, vl_preco_unitario_analise, vl_preco_total_analise, pc_bdi_licitado, vl_preco_unitario_licitado, evento_fk, in_fonte, adt_login, adt_data_hora, adt_operacao, id_servico_analise, versao_nr, versao_id, versao_nm_evento)  \n"
			+ "   VALUES( 1, 1, '', '',  '', '', 0, 0, 0, 0, 0, 0, 0, 0, NULL, 'SNP', 99999999999,  current_timestamp, 'INSERT', 0, 0, NULL, NULL);"
			+ "INSERT INTO siconv.vrpl_servico ( macro_servico_fk, nr_servico,  tx_observacao, cd_servico, tx_descricao, sg_unidade, vl_custo_unitario_ref_analise, pc_bdi_analise, qt_total_itens_analise, vl_custo_unitario_analise, vl_preco_unitario_analise, vl_preco_total_analise, pc_bdi_licitado, vl_preco_unitario_licitado, evento_fk, in_fonte, adt_login, adt_data_hora, adt_operacao, id_servico_analise, versao_nr, versao_id, versao_nm_evento)  \n"
			+ "   VALUES( 2, 1, '', '',  '', '', 0, 0, 0, 0, 0, 0, 0, 0, NULL, 'SNP', 99999999999,  current_timestamp, 'INSERT', 0, 0, NULL, NULL);";

	public static LicitacaoBD criaLicitacao() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setIdentificadorDaProposta(1L);
		licitacao.setNumeroAno("00012019");
		licitacao.setSituacaoDaLicitacao("EPE");
		licitacao.setSituacaoDoProcessoLicitatorio("ELA");
		licitacao.setValorProcessoLicitatorio(BigDecimal.valueOf(120000.87));
		licitacao.setIdLicitacaoFk(1L);
		licitacao.setObjeto("objeto");

		return licitacao;
	}

	public static PoBD criarPO() {

		PoBD po = new PoBD();
		po.setDtBaseAnalise(LocalDate.now());
		po.setDtPrevisaoInicioObraAnalise(LocalDate.now());
		po.setDtPrevisaoInicioObra(LocalDate.now());
		po.setIdLicitacao(1L);
		po.setIdPoAnalise(1L);
		po.setInAcompanhamentoEventos(true);
		po.setInDesonerado(true);
		po.setInTrabalhoSocial(false);
		po.setMetaDescricao("tx_descricao_analise0");
		po.setMetaNumero(0L);
		po.setPrecoTotalAnalise(new BigDecimal(1000));
		po.setQtMesesDuracaoObra(3L);
		po.setSgLocalidade("PE");
		po.setSubmetaAnaliseId(1L);
		po.setSubmetaDescricao("tx_descricao_analise0");
		po.setSubmetaId(1L);
		po.setSubmetaNumero(1L);
		po.setSubmetaVlContrapartida(new BigDecimal(1000));
		po.setSubmetaVlOutros(new BigDecimal(0));
		po.setSubmetaVlRepasse(new BigDecimal(9000));
		po.setSubmetaVlTotalLicitado(new BigDecimal(10000));
		po.setVersao(1L);
		return po;

	}

	public static AnexoBD criaAnexo(Long identificadorDaLicitacao) {
		AnexoBD anexo = new AnexoBD();
		anexo.setCaminhoDoArquivo("caminho/pasta/");
		anexo.setBucket("teste-unitario");
		anexo.setCpfDoUsuarioQueEnviou("01107221404");
		anexo.setDescricaoDoAnexo("Descricao");
		anexo.setIdentificadorDaLicitacao(identificadorDaLicitacao);
		anexo.setNomeDoArquivo("anexo.txt");
		anexo.setNomeDoUsuarioQueEnviou("Leonardo");
		anexo.setPerfilDoUsuarioQueEnviou(Profile.PROPONENTE.name());
		anexo.setTipoDoAnexo(TipoAnexoEnum.ATA_HOMOLOGACAO_LICITACAO);
		return anexo;
	}

	public static MacroServicoBD criaMacroServico(Long idPo) {
		MacroServicoBD macro = new MacroServicoBD();
		macro.setIdMacroServicoAnalise(1L);
		macro.setNrMacroServico(1L);
		macro.setPoFk(idPo);
		macro.setTxDescricao("Macroservico");
		macro.setVersao(1L);

		return macro;
	}

	public static List<ServicoBD> criaServicos(Long eventoFk, Long macroServicoFk) {
		List<ServicoBD> servicos = new ArrayList<>();

		ServicoBD s1 = new ServicoBD();
		s1.setCdServico("1");
		s1.setEventoFk(eventoFk);
		s1.setIdServicoAnalise(1L);
		s1.setInFonte("FON");
		s1.setMacroServicoFk(macroServicoFk);
		s1.setNrServico(1);
		s1.setPcBdi(BigDecimal.ONE);
		s1.setPcBdiLicitado(BigDecimal.ONE);
		s1.setQtTotalItensAnalise(BigDecimal.ONE);
		s1.setSgUnidade("PE");
		s1.setTxDescricao("Descrição");
		s1.setTxObservacao("Observação");
		s1.setVlCustoUnitario(BigDecimal.ONE);
		s1.setVlCustoUnitarioRef(BigDecimal.ONE);
		s1.setVlPrecoTotal(BigDecimal.ONE);
		s1.setVlPrecoUnitario(BigDecimal.ONE);
		s1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos.add(s1);

		return servicos;
	}

	public static ServicoFrenteObraBD criaServicoFrenteObra(Long frenteObraFk, Long servicoFk) {
		ServicoFrenteObraBD sfo = new ServicoFrenteObraBD();
		sfo.setFrenteObraFk(frenteObraFk);
		sfo.setQtItens(BigDecimal.ONE);
		sfo.setServicoFk(servicoFk);
		return sfo;
	}

	public static EventoFrenteObraBD criaEventoFrenteObra(Long eventoId, Long frenteId) {
		EventoFrenteObraBD efo = new EventoFrenteObraBD();
		efo.setEventoFk(eventoId);
		efo.setFrenteObraFk(frenteId);
		efo.setNrMesConclusao(1);

		return efo;
	}

	public static FrenteObraBD criaFrente(Long idPo) {
		FrenteObraBD frente = new FrenteObraBD();
		frente.setIdAnalise(1L);
		frente.setNmFrenteObra("Frente");
		frente.setNrFrenteObra(1);
		frente.setPoFk(idPo);
		frente.setQtdServicoFrenteObra(BigDecimal.ONE);
		frente.setQtdServicoFrenteObraAnalise(BigDecimal.ONE);
		frente.setVersao(0L);

		return frente;
	}

	public static String dependenciasPo() {
		String res = "insert into siconv.vrpl_proposta (id, id_siconv, numero_proposta, ano_proposta, valor_global, valor_repasse, valor_contrapartida, modalidade, nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa, nome_programa, identificacao_proponente, nome_proponente, uf, pc_min_contrapartida, nome_mandataria, categoria, nivel_contrato, adt_login, adt_data_hora, adt_operacao, versao, versao_id, versao_nm_evento, versao_nr, apelido_empreendimento)\n"
				+ "                              values( 1,  1, 26657, 8248, -1172461824.16, -1204048512.29, 1.00, 1, 'tempor incididunt ut labore et dolore magna aliqua. ', 23582, 21212, '1993-11-08', 'esse cill', 'esse cillum dolore eu fugiat nulla pariatur. Excep', '1', 'elit, sed do eiusmod tem', 'id', 1.00, 'sed do ei', 'adipi', 'amet, c', 'eu f', '1990-04-05 06:06:20.558', 'INSERT', 7596, '1', 'cu', 0, 'apelido');\n"
				+ "insert into siconv.vrpl_licitacao (objeto, proposta_fk, numero_ano, adt_login, adt_data_hora, in_situacao, versao_id, versao, valor_processo, id_licitacao_fk, versao_nm_evento, versao_nr, adt_operacao, status_processo, id)\n"
				+ "values ('objeto' , 1, 2018, 'USUARIO - TESTE UNITARIO', localtimestamp, 'HOM', 0, 0, 1.45, 1, 'HOM', 0, 'INSERT', true, nextval('siconv.vrpl_licitacao_seq'));\n"
				+ "insert into siconv.vrpl_lote_licitacao (versao_nm_evento, adt_login, adt_data_hora, licitacao_fk, versao_nr, adt_operacao, numero_lote, versao_id, versao, id)\n"
				+ "values ( null, 'Usuario - Testes Unitarios', now(), currval('siconv.vrpl_licitacao_seq') - 0, 0, 'INSERT', 1, null, 0, nextval('siconv.vrpl_lote_licitacao_seq'));\n"
				+ "insert into siconv.vrpl_subitem_investimento (descricao_und, adt_login, adt_data_hora, codigo_und, versao_id, versao, descricao, versao_nm_evento, in_projeto_social, versao_nr, adt_operacao, id_subitem_analise, id)\n"
				+ "values ( null, 'Usuario - Testes Unitarios', now(), null, null, 0, 'descricao0', null, null, 0, 'INSERT', 0, nextval('siconv.vrpl_subitem_investimento_seq'));\n"
				+ "insert into siconv.vrpl_meta (adt_login, adt_data_hora, subitem_fk, nr_meta_analise, qt_itens_analise, versao_id, in_social, versao, versao_nm_evento, id_meta_analise, versao_nr, tx_descricao_analise, adt_operacao, id)\n"
				+ "values ( 'Usuario - Testes Unitarios', now(), currval('siconv.vrpl_subitem_investimento_seq') - 0, 0, 0, null, false, 0, null, 0, 0, 'tx_descricao_analise0', 'INSERT', nextval('siconv.vrpl_meta_seq'));\n"
				+ "insert into siconv.vrpl_submeta (proposta_fk, vl_total_analise, id_submeta_analise, vl_contrapartida_analise, in_regime_execucao, nr_submeta_analise,                             meta_fk, vl_outros_analise, versao_nm_evento, adt_operacao, vl_repasse,                        vrpl_lote_licitacao_fk, vl_repasse_analise,     adt_login, adt_data_hora, nr_lote_analise, in_situacao, vl_contrapartida, in_situacao_analise, versao_id, vl_total_licitado, versao, versao_nr,    tx_descricao_analise, natureza_despesa_sub_it_fk_analise, in_regime_execucao_analise, vl_outros,                                id)\n"
				+ " values (          1,     0,                  1,                        0,              'TRF',                  1, currval('siconv.vrpl_meta_seq') - 0,                 0,             null,     'INSERT',          0, currval('siconv.vrpl_lote_licitacao_seq') - 0,                  0, '11111111111',         now(),               0,        'G0',                0,                'G0',         1,                 0,       0,         0, 'tx_descricao_analise0',                                  0,                       null,         0, nextval('siconv.vrpl_submeta_seq'));";

		return res;
	}

	public static String dependenciasLaudoGrupoPergunta() {
		String res = "Insert into siconv.vrpl_template_laudo (tipo, versao_nm_evento,    adt_login, adt_data_hora, versao_nr, adt_operacao, versao_id, versao,                                       id) "
				+ "                                   values (null,             null,'11111111111',          null,         0,     'INSERT',         1,       0, nextval('siconv.vrpl_template_laudo_seq')); ";

		res += " Insert into siconv.vrpl_grupo_pergunta (    adt_login,                        adt_data_hora, numero, in_grupo_obrigatorio,    titulo, versao_id, versao,                                   template_fk, versao_nm_evento, versao_nr, adt_operacao,                                        id) "
				+ "                              values ('11111111111', to_date ('17/05/2019', 'DD/MM/YYYY'),      0,                false, 'titulo0',         1,       0, currval('siconv.vrpl_template_laudo_seq') - 0,            'INI',         1,     'INSERT', nextval('siconv.vrpl_grupo_pergunta_seq')); ";

		res += " Insert into siconv.vrpl_grupo_pergunta (    adt_login,                         adt_data_hora, numero, in_grupo_obrigatorio,    titulo, versao_id, versao,                                   template_fk, versao_nm_evento, versao_nr, adt_operacao,                                        id) "
				+ "                              values ('11111111111',  to_date ('17/05/2019', 'DD/MM/YYYY'),      1,                true,  'titulo1',         2,       0, currval('siconv.vrpl_template_laudo_seq') - 0,           'INI',         1,     'INSERT', nextval('siconv.vrpl_grupo_pergunta_seq')); ";

		res += "insert into siconv.vrpl_proposta (id, id_siconv, numero_proposta, ano_proposta,   valor_global,  valor_repasse, valor_contrapartida, modalidade,                                            nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa,                                        nome_programa, identificacao_proponente,            nome_proponente,   uf, pc_min_contrapartida, nome_mandataria, categoria, nivel_contrato, adt_login,             adt_data_hora, adt_operacao, versao, versao_id, versao_nm_evento, versao_nr, apelido_empreendimento)"
				+ "                        values( 1,         1, 26657,         8248, -1172461824.16, -1204048512.29,                1.00,          1, 'tempor incididunt ut labore et dolore magna aliqua. ',           23582,        21212,             '1993-11-08',     'esse cill', 'esse cillum dolore eu fugiat nulla pariatur. Excep',                      '1', 'elit, sed do eiusmod tem', 'id',                 1.00,     'sed do ei',   'adipi',      'amet, c',    'eu f', '1990-04-05 06:06:20.558',     'INSERT',    7596,       '1',             'cu',      8566, 'apelido');";

		res += "Insert into siconv.vrpl_licitacao (objeto, proposta_fk, numero_ano,                  adt_login,  adt_data_hora, in_situacao, versao_id, versao, valor_processo, id_licitacao_fk, versao_nm_evento, versao_nr, adt_operacao, status_processo, id)"
				+ "                                    values ('objeto',         1,       2018, 'USUARIO - TESTE UNITARIO', LOCALTIMESTAMP,      'HOM',         0,       0,           1.45,                1,            'HOM',         0,               'INSERT',            true, nextval('siconv.vrpl_licitacao_seq')); ";

		res += " Insert into siconv.vrpl_laudo (    adt_login,                        adt_data_hora, versao_id, versao,                                   template_fk, versao_nm_evento,                                   licitacao_fk, versao_nr, adt_operacao, in_resultado, in_status,                               id) "
				+ "                     values ('11111111111', to_date ('17/05/2019', 'DD/MM/YYYY'),         1,       0, currval('siconv.vrpl_template_laudo_seq') - 0,             null, currval('siconv.vrpl_licitacao_seq') - 0,         0,     'INSERT',         null,         0, nextval('siconv.vrpl_laudo_seq')); ";

		return res;
	}

	public static String dependenciasPLQ() {
		return " Insert into siconv.vrpl_proposta (pc_min_contrapartida, id_siconv, nivel_contrato, nome_mandataria, nome_proponente,   uf, versao_nm_evento, ano_proposta, valor_contrapartida, versao_in_atual, adt_operacao, identificacao_proponente, numero_proposta, ano_convenio, numero_convenio,    adt_login,                        adt_data_hora,    nome_programa, codigo_programa,    categoria, nome_objeto, versao_id, versao, valor_global, valor_repasse, versao_nr, data_assinatura_convenio, spa_homologado, modalidade,                                  id, apelido_empreendimento) \n"
				+ "                        values (                null,         0,           null,            null,            null, 'G0',             null,            0,                   0,           false,     'UPDATE',                     null,               0,         null,            null, 'adt_login0', to_date ('28/05/2019', 'DD/MM/YYYY'), 'nome_programa0',            'G0', 'categoria0',        null,      null,       0,            0,             0,         0,                     null,          false,          0, nextval('siconv.vrpl_proposta_seq'), 'apelido'); \n"

				+ " Insert into siconv.vrpl_licitacao (proposta_fk,    numero_ano,    adt_login,                        adt_data_hora, in_situacao,    objeto, versao_id, versao, valor_processo, id_licitacao_fk, versao_nm_evento, versao_nr, adt_operacao,    status_processo,                                   id) \n"
				+ "                              values (currval('siconv.vrpl_proposta_seq'), 'numero_ano0', 'adt_login0', to_date ('28/05/2019', 'DD/MM/YYYY'),        'G0', 'objeto0',      null,    0,              0,               0,             null,         0,     'INSERT', 'status_processo0', nextval('siconv.vrpl_licitacao_seq')); \n"

				+ " Insert into siconv.vrpl_lote_licitacao (versao_nm_evento,    adt_login,                        adt_data_hora,                              licitacao_fk, versao_nr, adt_operacao, numero_lote, versao_id, versao,                                        id)\n"
				+ "                                 values (            null, 'adt_login0', to_date ('28/05/2019', 'DD/MM/YYYY'), currval('siconv.vrpl_licitacao_seq'),         0,     'INSERT',           0,      null,    0, nextval('siconv.vrpl_lote_licitacao_seq')); \n"

				+ " Insert into siconv.vrpl_subitem_investimento (descricao_und,    adt_login,                        adt_data_hora, codigo_und, versao_id, versao,    descricao, versao_nm_evento, in_projeto_social, versao_nr, adt_operacao, id_subitem_analise,                                              id) \n"
				+ "                                       values (         null, 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),       null,      null,    0, 'descricao0',             null,              null,         0,     'INSERT',                  0, nextval('siconv.vrpl_subitem_investimento_seq'));  \n"

				+ " Insert into siconv.vrpl_meta (   adt_login,                        adt_data_hora,                                           subitem_fk, nr_meta_analise, qt_itens_analise, versao_id, in_social, versao, versao_nm_evento, id_meta_analise, versao_nr,    tx_descricao_analise, adt_operacao,                              id)  \n"
				+ "                       values ('adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'), currval('siconv.vrpl_subitem_investimento_seq'),               0,                0,      null,     false,    0,             null,               0,         0, 'tx_descricao_analise0',     'INSERT', nextval('siconv.vrpl_meta_seq'));  \n"

				+ " Insert into siconv.vrpl_submeta (vl_total_analise, id_submeta_analise, vl_contrapartida_analise, in_regime_execucao, nr_submeta_analise,                         meta_fk, vl_outros_analise, versao_nm_evento, adt_operacao, vl_repasse,                    vrpl_lote_licitacao_fk, vl_repasse_analise,                         proposta_fk,    adt_login,                        adt_data_hora, nr_lote_analise, in_situacao, vl_contrapartida, in_situacao_analise, versao_id, vl_total_licitado, versao, versao_nr,    tx_descricao_analise, natureza_despesa_sub_it_fk_analise, in_regime_execucao_analise, vl_outros,                                 id) \n"
				+ "                          values (               0,                  0,                        0,              'EPG',                  0, currval('siconv.vrpl_meta_seq'),              null,             null,     'INSERT',          0, currval('siconv.vrpl_lote_licitacao_seq'),                  0, currval('siconv.vrpl_proposta_seq'), 'adt_login0', to_date ('28/05/2019', 'DD/MM/YYYY'),               0,        'EPE',                0,               'HOM',      null,                 0,       0,         0, 'tx_descricao_analise0',                                  0,                      'EPG',         0, nextval('siconv.vrpl_submeta_seq')); \n"
				+ " Insert into siconv.vrpl_submeta (vl_total_analise, id_submeta_analise, vl_contrapartida_analise, in_regime_execucao, nr_submeta_analise,                         meta_fk, vl_outros_analise, versao_nm_evento, adt_operacao, vl_repasse,                    vrpl_lote_licitacao_fk, vl_repasse_analise,                         proposta_fk,    adt_login,                        adt_data_hora, nr_lote_analise, in_situacao, vl_contrapartida, in_situacao_analise, versao_id, vl_total_licitado, versao, versao_nr,    tx_descricao_analise, natureza_despesa_sub_it_fk_analise, in_regime_execucao_analise, vl_outros,                                 id) \n"
				+ "                          values (               1,                  1,                        1,              'EPG',                  1, currval('siconv.vrpl_meta_seq'),              null,             null,     'INSERT',          1, currval('siconv.vrpl_lote_licitacao_seq'),                  1, currval('siconv.vrpl_proposta_seq'), 'adt_login1', to_date ('28/05/2019', 'DD/MM/YYYY'),               1,        'EPE',                1,               'HOM',      null,                 1,       1,         1, 'tx_descricao_analise1',                                  1,                      'EPG',         1, nextval('siconv.vrpl_submeta_seq')); \n"

				+ " Insert into siconv.vrpl_po (             dt_previsao_inicio_obra, dt_previsao_inicio_obra_analise,    adt_login,                        adt_data_hora, qt_meses_duracao_obra, in_acompanhamento_eventos, sg_localidade,                              dt_base_analise, versao_id, versao, versao_nm_evento, versao_nr,                              submeta_fk, adt_operacao, in_desonerado, id_po_analise,                            id)  \n"
				+ "                     values (to_date ('27/05/2019', 'DD/MM/YYYY'), to_date ('27/05/2019', 'DD/MM/YYYY'), 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),                     0,                      true,          null, to_date ('27/05/2019', 'DD/MM/YYYY'),      null,    0,             null,         0, currval('siconv.vrpl_submeta_seq') - 0,     'UPDATE',          null,             0, nextval('siconv.vrpl_po_seq'));  \n"
				+ " Insert into siconv.vrpl_po (             dt_previsao_inicio_obra, dt_previsao_inicio_obra_analise,    adt_login,                        adt_data_hora, qt_meses_duracao_obra, in_acompanhamento_eventos, sg_localidade,                              dt_base_analise, versao_id, versao, versao_nm_evento, versao_nr,                              submeta_fk, adt_operacao, in_desonerado, id_po_analise,                            id)  \n"
				+ "                     values (to_date ('27/05/2019', 'DD/MM/YYYY'),to_date ('27/05/2019', 'DD/MM/YYYY'), 'adt_login1', to_date ('27/05/2019', 'DD/MM/YYYY'),                     1,                     false,          null, to_date ('27/05/2019', 'DD/MM/YYYY'),      null,    0,             null,         1, currval('siconv.vrpl_submeta_seq') - 1,     'DELETE',          null,             1, nextval('siconv.vrpl_po_seq'));  \n"

				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             0,             null,   'LOGIN', current_timestamp,         0, 'INSERT', 'nm_frente_obra0', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             1,             null,   'LOGIN', current_timestamp,         1, 'INSERT', 'nm_frente_obra1', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             2,             null,   'LOGIN', current_timestamp,         2, 'INSERT', 'nm_frente_obra2', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             3,             null,   'LOGIN', current_timestamp,         3, 'INSERT', 'nm_frente_obra3', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             4,             null,   'LOGIN', current_timestamp,         4, 'INSERT', 'nm_frente_obra4', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             5,             null,   'LOGIN', current_timestamp,         5, 'INSERT', 'nm_frente_obra5', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             6,             null,   'LOGIN', current_timestamp,         6, 'INSERT', 'nm_frente_obra6', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                              po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (             7,             null,   'LOGIN', current_timestamp,         7, 'INSERT', 'nm_frente_obra7', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                             po_fk, versao_id, versao,                                     id)   \n"
				+ "                              values (             8,             null,   'LOGIN', current_timestamp,         8, 'INSERT', 'nm_frente_obra8', currval('siconv.vrpl_po_seq') - 1,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,    nm_frente_obra,                             po_fk, versao_id, versao,                                     id)   \n"
				+ "                              values (             9,             null,   'LOGIN', current_timestamp,         9, 'INSERT', 'nm_frente_obra9', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq'));  \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            10,             null,   'LOGIN', current_timestamp,        10, 'INSERT', 'nm_frente_obra10', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            11,             null,   'LOGIN', current_timestamp,        11, 'INSERT', 'nm_frente_obra11', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            12,             null,   'LOGIN', current_timestamp,        12, 'INSERT', 'nm_frente_obra12', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            13,             null,   'LOGIN', current_timestamp,        13, 'INSERT', 'nm_frente_obra13', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            14,             null,   'LOGIN', current_timestamp,        14, 'INSERT', 'nm_frente_obra14', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            15,             null,   'LOGIN', current_timestamp,        15, 'INSERT', 'nm_frente_obra15', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            16,             null,   'LOGIN', current_timestamp,        16, 'INSERT', 'nm_frente_obra16', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"
				+ " Insert into siconv.vrpl_frente_obra (nr_frente_obra, versao_nm_evento, adt_login, adt_data_hora, versao_nr, adt_operacao,     nm_frente_obra,                             po_fk, versao_id, versao,                                     id)  \n"
				+ "                              values (            17,             null,   'LOGIN', current_timestamp,        17, 'INSERT', 'nm_frente_obra17', currval('siconv.vrpl_po_seq') - 0,      null,    0, nextval('siconv.vrpl_frente_obra_seq')); \n"

				+ " Insert into siconv.vrpl_evento (versao_nm_evento,    adt_login,                        adt_data_hora, nr_evento, versao_nr, adt_operacao,    nm_evento,                         po_fk, versao_id, versao,                                id)  \n"
				+ "                         values (            null, 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),         0,         0,     'DELETE', 'nm_evento0', currval('siconv.vrpl_po_seq'),      null,       0, nextval('siconv.vrpl_evento_seq')); \n"
				+ " Insert into siconv.vrpl_evento (versao_nm_evento,    adt_login,                        adt_data_hora, nr_evento, versao_nr, adt_operacao,    nm_evento,                         po_fk, versao_id, versao,                                id)  \n"
				+ "                         values (            null, 'adt_login1', to_date ('27/05/2019', 'DD/MM/YYYY'),         1,         1,     'INSERT', 'nm_evento1', currval('siconv.vrpl_po_seq'),      null,       1, nextval('siconv.vrpl_evento_seq')); \n"

				+ " Insert into siconv.vrpl_macro_servico (versao_nm_evento,    adt_login,                        adt_data_hora, versao_nr,    tx_descricao, adt_operacao,                             po_fk, id_macro_servico_analise, versao_id, versao, nr_macro_servico,                                  id)  \n"
				+ "                                values (            null, 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),         0, 'tx_descricao0',     'DELETE', currval('siconv.vrpl_po_seq') - 1,                        0,      null,    0,                0, nextval('siconv.vrpl_macro_servico_seq')); \n"
				+ " Insert into siconv.vrpl_macro_servico (versao_nm_evento,    adt_login,                        adt_data_hora, versao_nr,    tx_descricao, adt_operacao,                             po_fk, id_macro_servico_analise, versao_id, versao, nr_macro_servico,                                          id) \n"
				+ "                                values (            null, 'adt_login1', to_date ('27/05/2019', 'DD/MM/YYYY'),         1, 'tx_descricao1',     'DELETE', currval('siconv.vrpl_po_seq') - 0,                        1,      null,    0,                1, nextval('siconv.vrpl_macro_servico_seq')); \n"

				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado,                              evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                             macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                       100, currval('siconv.vrpl_evento_seq') ,                  0,             null, 'cd_servico0', 'tx_descricao0',     'INSERT',                       100,          0, currval('siconv.vrpl_macro_servico_seq') - 1,                        100, 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                    100,          null,   'OUT',      null,    0,                  10000,                           100,         0,                  0,       'G0', nextval('siconv.vrpl_servico_seq')); \n"
				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado,                              evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                             macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                      1000, currval('siconv.vrpl_evento_seq') - 1,               0,             null, 'cd_servico1', 'tx_descricao1',     'INSERT',                      1000,          1, currval('siconv.vrpl_macro_servico_seq') - 1,                       1000, 'adt_login1', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                    100,          null,   'OUT',      null,    0,                 100000,                          1000,         1,                  1,       'G1', nextval('siconv.vrpl_servico_seq')); \n"
				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado,                              evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                             macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                     10000, currval('siconv.vrpl_evento_seq') - 0,               0,             null, 'cd_servico2', 'tx_descricao2',     'INSERT',                     10000,          2, currval('siconv.vrpl_macro_servico_seq') - 1,                      10000, 'adt_login2', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                    100,          null,   'OUT',      null,    0,                1000000,                         10000,         2,                  2,       'G2', nextval('siconv.vrpl_servico_seq')); \n"
				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado, evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                              macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                       100,      null,              0,             null, 'cd_servico3', 'tx_descricao3',     'INSERT',                       100,          3, currval('siconv.vrpl_macro_servico_seq') - 0,                        100, 'adt_login3', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                     100,          null,   'OUT',      null,    0,                  10000,                           100,         3,                  3,       'G3', nextval('siconv.vrpl_servico_seq')); \n"
				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado, evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                              macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                      1000,      null,              0,             null, 'cd_servico4', 'tx_descricao4',     'INSERT',                      1000,          4, currval('siconv.vrpl_macro_servico_seq') - 0,                       1000, 'adt_login4', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                     100,          null,   'OUT',      null,    0,                 100000,                          1000,         4,                  4,       'G4', nextval('siconv.vrpl_servico_seq')); \n"
				+ " Insert into siconv.vrpl_servico (vl_preco_unitario_licitado, evento_fk, pc_bdi_analise, versao_nm_evento,    cd_servico,    tx_descricao, adt_operacao, vl_preco_unitario_analise, nr_servico,                              macro_servico_fk, vl_custo_unitario_analise,    adt_login,                        adt_data_hora, pc_bdi_licitado, qt_total_itens_analise, tx_observacao, in_fonte, versao_id, versao, vl_preco_total_analise, vl_custo_unitario_ref_analise, versao_nr, id_servico_analise, sg_unidade,                                 id)  \n"
				+ "                          values (                     10000,      null,              0,             null, 'cd_servico5', 'tx_descricao5',     'INSERT',                     10000,          5, currval('siconv.vrpl_macro_servico_seq') - 0,                      10000, 'adt_login5', to_date ('27/05/2019', 'DD/MM/YYYY'),               0,                     100,          null,   'OUT',      null,    0,                1000000,                         10000,         5,                  5,       'G5', nextval('siconv.vrpl_servico_seq')); \n"

				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                         servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 17,       10,             null, currval('siconv.vrpl_servico_seq'), 'adt_login0', to_date ('27/05/2019', 'DD/MM/YYYY'),         0,     'INSERT',      null,    0);  \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                         servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 16,      70,             null, currval('siconv.vrpl_servico_seq'), 'adt_login1', to_date ('27/05/2019', 'DD/MM/YYYY'),         1,     'INSERT',       null,    0);  \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                        servico_fk,    adt_login,                         adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 15,       20,             null, currval('siconv.vrpl_servico_seq'), 'adt_login2', to_date ('27/05/2019', 'DD/MM/YYYY'),         2,     'INSERT',       null,    0);  \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 14,        8,             null, currval('siconv.vrpl_servico_seq') - 1, 'adt_login3', to_date ('27/05/2019', 'DD/MM/YYYY'),         3,     'INSERT',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 13,       12,             null, currval('siconv.vrpl_servico_seq') - 1, 'adt_login4', to_date ('27/05/2019', 'DD/MM/YYYY'),         4,     'DELETE',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 12,      80,              null, currval('siconv.vrpl_servico_seq') - 1, 'adt_login5', to_date ('27/05/2019', 'DD/MM/YYYY'),         5,     'DELETE',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 11,       16,             null, currval('siconv.vrpl_servico_seq') - 2, 'adt_login6', to_date ('27/05/2019', 'DD/MM/YYYY'),         6,     'UPDATE',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                             frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao) \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 10,       17,             null, currval('siconv.vrpl_servico_seq') - 2, 'adt_login7', to_date ('27/05/2019', 'DD/MM/YYYY'),         7,     'INSERT',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                              servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao)   \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 9,        67,            null, currval('siconv.vrpl_servico_seq') - 2,  'adt_login8', to_date ('27/05/2019', 'DD/MM/YYYY'),         8,     'INSERT',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,    adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao)   \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 8,       10,             null, currval('siconv.vrpl_servico_seq') - 3, 'adt_login9', to_date ('27/05/2019', 'DD/MM/YYYY'),         9,     'INSERT',        null,    0);  \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 7,       50,             null, currval('siconv.vrpl_servico_seq') - 3, 'adt_login10', to_date ('27/05/2019', 'DD/MM/YYYY'),        10,     'INSERT',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao,  versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 6,       40,             null, currval('siconv.vrpl_servico_seq') - 3, 'adt_login11', to_date ('27/05/2019', 'DD/MM/YYYY'),        11,     'INSERT',       null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 5,       10,             null, currval('siconv.vrpl_servico_seq') - 4, 'adt_login12', to_date ('27/05/2019', 'DD/MM/YYYY'),        12,     'INSERT',      null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 4,       30,             null, currval('siconv.vrpl_servico_seq') - 4, 'adt_login13', to_date ('27/05/2019', 'DD/MM/YYYY'),        13,     'INSERT',      null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 3,       60,             null, currval('siconv.vrpl_servico_seq') - 4, 'adt_login14', to_date ('27/05/2019', 'DD/MM/YYYY'),        14,     'INSERT',      null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 2,       32,             null, currval('siconv.vrpl_servico_seq') - 5, 'adt_login15', to_date ('27/05/2019', 'DD/MM/YYYY'),        15,     'INSERT',      null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 1,       29,             null, currval('siconv.vrpl_servico_seq') - 5, 'adt_login16', to_date ('27/05/2019', 'DD/MM/YYYY'),        16,     'INSERT',      null,    0); \n"
				+ " Insert into siconv.vrpl_servico_frente_obra (                            frente_obra_fk, qt_itens, versao_nm_evento,                             servico_fk,     adt_login,                        adt_data_hora, versao_nr, adt_operacao, versao_id, versao)  \n"
				+ "                                      values (currval('siconv.vrpl_frente_obra_seq') - 0,       39,             null, currval('siconv.vrpl_servico_seq') - 5, 'adt_login17', to_date ('27/05/2019', 'DD/MM/YYYY'),        17,     'INSERT',      null,    0); ";

	}

	public static String getConvenioInsert() {
		return "INSERT INTO SICONV.CONVENIO (id, hibernate_version, inicio_execucao, fim_execucao, data_assinatura, sequencial, ano, tipo_convenio, status_convenio, empenhado, publicado) "
				+ "VALUES (1, 1, '2020-01-01', '2020-01-02', '2020-01-09', 883675, 2020, 1, '1', true, true);";
	}

	public static String getPropostaInsert() {
		return
				"INSERT INTO siconv.prop " +
				"(id, hibernate_version, inicio_execucao, fim_execucao, justificativa, valor_global, valor_repasse, " //7
				+ "valor_contra_partida, valor_repasse_exercicio_atual, sequencial, ano, data_proposta, convenio_fk, " //13
				+ "executor_fk, objeto_fk, programa_fk, proponente_fk, valor_contrapartida_financeira, " //18
				+ "valor_contrapartida_bens_servi, org_administrativo_fk, modalidade, regras_fk, mandatario_fk, " // 22
				+ "propostas_mandataria_fk, objeto_convenio, conta_bancaria_fk, agencia, data_versao, adt_operacao, " // 28
				+ "adt_data_hora, adt_login, membro_partc_fk, ug_executora_fk, gestao, situacao_envio_contrato_repass, " // 34
				+ "capacidade_tecnica, teste_capacidade_tecnica, ver_at_fk, historico, empenho_publicacao, " // 40
				+ "instituicao_mandataria_fk, org_concedente_fk, org_executor_fk, data_entrega_projeto_basico, data_limite_complementacao_pb, data_limite_entrega_projeto_ba, permite_adiar_entrega_proj_bas, permite_liberar_primeiro_repas, permite_prorrogar_entrega_proj, prazo_entrega_proj_basico, situacao_projeto_basico, tipo_projeto_basico, projeto_basico_fk, data_ultimo_envio_contrato_rep, situacao_legado, data_aprovacao_plano_trabalho, data_aprovacao_de_proposta, atribuicao_resp_analise, complementacao, situacao_proposta, ug_contrato_publicacao_fk, tipo_nao_acatamento_contrato_r, gestao_contrato_publicacao, data_ultimo_retorno_contrato_r, condicionante_liberacao_primei, descricao_condicionante, assinatura_pendente_ajuste_cro, efetivo, enviada_para_instituicao, valor_aplicacao, fim_execucao_anterior_antecipa, anexo_termo_convenio_fk, justificativa_termo_convenio, numero_controle_externo, numero_processo_connect, data_confirmacao_entrega_caixa, pontuacao, ordenacao, justificativa_celebracao_antec, documentos_aprovados, documentos_aprovacao_observaca, documentos_aprovacao_data, primeira_analise_p_t_realizada, data_analise_projeto_basico) "
				+ "VALUES(1, 0, now(), now(), '', 0, 0, 0, 0, 0, 0, now(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, '', now(), " // 28
				+ "'ND'::character varying, now(), 'ND'::character varying, 0, 0, '', '', '', '', 0, false, ''" // 40
				+ ", 1, 0, 0, now(), now(), now(), false, false, false, 0, 'NAO_CADASTRADO'::character varying, '', 0, now(), 0, now(), now(), '', '', '', 0, '', '', now(), false, '', false, true, false, 0, now(), 0, NULL::character varying, "
				+ "'', 0, now(), 0, 0, '', false, '', now(), false, now()); ";
	}

	public static String getVrplProposta(Integer numeroConvenio, Integer anoConvenio, String dataAssinaturaConvenio, String nomeMandataria) {
		String dt = dataAssinaturaConvenio == null ? "null" : "'" + dataAssinaturaConvenio + "'";
		String nm = nomeMandataria == null ? "null" : "'" + nomeMandataria + "'";
		return 	"INSERT INTO SICONV.VRPL_PROPOSTA "
				+ "(id,id_siconv,numero_proposta,ano_proposta,valor_global,valor_repasse,valor_contrapartida,modalidade,nome_objeto," //9
				+ "numero_convenio,ano_convenio,data_assinatura_convenio,codigo_programa,nome_programa,identificacao_proponente,nome_proponente,uf,pc_min_contrapartida,"
				+ "nome_mandataria,categoria,nivel_contrato,spa_homologado,adt_login,adt_data_hora,adt_operacao,versao,versao_id,versao_nm_evento,versao_nr,versao_in_atual,apelido_empreendimento) "
				+ "VALUES "
				+ "(1,1332536,31650,2018,275837.26,270476.19,5361.07,2,'Execução',"
				+ numeroConvenio + "," + anoConvenio + ","
				+ dt + ",'5300020180033',"
				+ "'PLANEJAMENTO URBANO','33265943000103','MUNICIPIO DE NOVO JARDIM','TO',0.10,"
				+ nm + ",'OBRAS_SERVICOS_ENGENHARIA','CONTRATO_NIVEL_I',true,"
				+ "'00442999070',{ts '2019-10-04 15:06:23.873439'},'INSERT',1,null,null,0,true,'PAVIMENTAÇÃO');";

	}

	public static String getAcffo() {
		return "INSERT INTO siconv.acffo\n" +
				"(id, tx_apelido, prop_fk, nr_versao_hibernate, in_situacao, versao_nr, versao_id, versao_nm_evento)\n" +
				"VALUES(1, 'ape', 1, 0, 'HOM', 0, '', null); " +

				"INSERT INTO siconv.acffo_proposta " +
				"(id, prop_siconv_id, numero_proposta, ano_proposta, valor_global, valor_repasse, valor_contra_partida, "
				+ "modalidade, nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa, nome_programa, identificacao_proponente, nome_proponente, uf, pc_min_contra_partida, nome_mandataria, categoria, nr_versao_hibernate, nivel_contrato, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 1, 2018, 0, 0, 0, 0, '', null, null, null, '', '', '', '', '', 0, '', '', 0, '', 0, '', ''); " +

				"INSERT INTO siconv.acffo_qci " +
				"(id, acffo_fk, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_meta " +
				"(id, tx_descricao, qt_itens, subitem_investimento_fk, qci_fk, nr_versao_hibernate, nr_meta, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 1, 0, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_submeta " +
				"(id, tx_descricao, nr_lote, vl_repasse, vl_contrapartida, vl_outros, meta_fk, vl_total, nr_versao_hibernate, nr_submeta, in_situacao, in_regime_execucao, natureza_despesa_sub_it_fk, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 0, 0, 0, 1, 0, 0, 1, '', 'EPG', 0, 1, '', ''); " +

				"INSERT INTO siconv.acffo_po " +
				"(id, dt_base, submeta_fk, in_desonerado, sg_localidade, dt_previsao_inicio_obra, qt_meses_duracao_obra, nr_versao_hibernate, in_acompanhamento_eventos, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, now(), 1, false, '', now(), 1, 0, false, 0, '', ''); " +

				"INSERT INTO siconv.acffo_evento " +
				"(id, nm_evento, po_fk, nr_evento, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_frente_obra " +
				"(id, nm_frente_obra, po_fk, nr_frente_obra, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 0, 0, '', ''); ";
	}

	public static String getOrgAdministrativoInsert() {
		return "INSERT INTO SICONV.ORG_ADMINISTRATIVO (id,hibernate_version,codigo,nome,org_superior_fk,"
				+ "tipo_orgao,adt_operacao,adt_data_hora,adt_login,instituicao_mandataria,banco_fk,bancolog,"
				+ "membro_padr_fk,mandatario_define_agencia_na_a,recebe_proposta_via_siconv,"
				+ "info_envio_prop_fora_siconv,cod_siorg) "
				+ "VALUES "
				+ "(1,1,'25220','CAIXA ECONOMICA FEDERAL',1453,'2','ATUALIZACAO',"
				+ "{ts '2019-07-25 11:43:12.925000'},'00513152032',true,1002,null,62333,true,true,'','');";
	}

	public static String defineUsuarioVRPL(String cpf) {
		return "SET LOCAL vrpl.cpf_usuario TO '" + cpf + "'";
	}

	public static String getInsertLicicacaoAceita() {
		return "INSERT INTO siconv.vrpl_licitacao (proposta_fk,numero_ano,objeto,valor_processo,status_processo,id_licitacao_fk,adt_login,adt_data_hora,adt_operacao,in_situacao,versao_nr,versao_id,versao_nm_evento,versao) VALUES\n"
				+ " (1,'Servitio partum volens accipi VRPL','sssssssssss',-340205120.52,'et dolore magna',16,'SISTEMA','2016-11-19 13:50:47.982','INSERT','ACT',17156,'16','ACT',0)";
	}
}
