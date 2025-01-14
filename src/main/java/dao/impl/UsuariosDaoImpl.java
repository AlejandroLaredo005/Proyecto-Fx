package dao.impl;

import dao.UsuariosDao;
import models.Usuarios;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class UsuariosDaoImpl implements UsuariosDao {

    @Override
    public void save(Usuarios entity) {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Inicia una transacción
            transaction = session.beginTransaction();
            session.persist(entity);  // Guarda la entidad en la base de datos
            transaction.commit();  // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Revertir la transacción en caso de error
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }
    }

    @Override
    public void update(Usuarios entity) {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Inicia una transacción
            transaction = session.beginTransaction();
            session.merge(entity);  // Actualiza la entidad en la base de datos
            transaction.commit();  // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Revertir la transacción en caso de error
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }
    }

    @Override
    public void delete(String id) {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Inicia una transacción
            transaction = session.beginTransaction();
            Usuarios usuario = session.get(Usuarios.class, id);  // Busca la entidad por su id

            if (usuario != null) {
                session.remove(usuario);  // Elimina la entidad de la base de datos
                transaction.commit();  // Confirma la transacción
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Revertir la transacción en caso de error
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }
    }

    @Override
    public Optional<Usuarios> findById(String id) {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuarios usuario = null;

        try {
            usuario = session.get(Usuarios.class, id);  // Busca la entidad por su id
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }

        return Optional.ofNullable(usuario);  // Devuelve un Optional con el resultado
    }

    @Override
    public List<Usuarios> findAll() {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuarios> usuarios = null;

        try {
            // Ejecuta una consulta HQL para obtener todos los usuarios
            usuarios = session.createQuery("FROM Usuarios", Usuarios.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }

        return usuarios;  // Devuelve la lista de usuarios
    }

    public Optional<Usuarios> findByUsuario(String nombreUsuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuarios usuario = null;

        try {
            usuario = session.createQuery("FROM Usuarios WHERE nombreUsuario = :nombreUsuario", Usuarios.class)
                             .setParameter("nombreUsuario", nombreUsuario)
                             .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return Optional.ofNullable(usuario);
    }

    // Método para buscar un usuario por su correo
    public Optional<Usuarios> findByCorreo(String correo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuarios usuario = null;

        try {
            usuario = session.createQuery("FROM Usuarios WHERE correo = :correo", Usuarios.class)
                             .setParameter("correo", correo)
                             .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return Optional.ofNullable(usuario);
    }
}
