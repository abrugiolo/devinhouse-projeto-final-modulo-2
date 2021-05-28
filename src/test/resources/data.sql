insert into interessado (nm_interessado, nu_identificacao, dt_nascimento, fl_ativo)
values ('Joao', '12345678901', '2000-05-19', 's'),
       ('Maria', '12345678902', '1998-05-19', 's');
 
insert into assunto (descricao, dt_cadastro, fl_ativo)
values ('Autorização para Corte de Árvores - Área Pública', '2021-05-19', 's'),
        ('Autorização para Utilização de Espaço Público', '2021-05-19', 's');
        
insert into processo (chave_processo, descricao, nu_ano, nu_processo, sg_orgao_setor, id_assunto, id_interessado)
values ('SOFT 1/2021', 'Processo', '2021', '1', 'soft', 1, 1),
       ('SOFT 2/2021', 'Processo', '2021', '2', 'soft', 2, 2);