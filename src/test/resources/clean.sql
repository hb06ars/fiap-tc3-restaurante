DELETE FROM public.avaliacao;
DELETE FROM public.reserva;
DELETE FROM public.funcionamento;
DELETE FROM public.mesa;
DELETE FROM public.restaurante;
DELETE FROM public.usuario;

ALTER SEQUENCE public.avaliacao_id_seq RESTART WITH 1;
ALTER SEQUENCE public.reserva_id_seq RESTART WITH 1;
ALTER SEQUENCE public.funcionamento_id_seq RESTART WITH 1;
ALTER SEQUENCE public.mesa_id_seq RESTART WITH 1;
ALTER SEQUENCE public.restaurante_id_seq RESTART WITH 1;
ALTER SEQUENCE public.usuario_id_seq RESTART WITH 1;
