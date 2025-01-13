package dao.impl;

import dao.JuegosDao;
import models.Juegos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class JuegosDaoImpl implements JuegosDao {

    @Override
    public void save(Juegos entity) {
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
    public void update(Juegos entity) {
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
            Juegos juego = session.get(Juegos.class, id);  // Busca la entidad por su id

            if (juego != null) {
                session.remove(juego);  // Elimina la entidad de la base de datos
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
    public Optional<Juegos> findById(String id) {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Juegos juego = null;

        try {
            juego = session.get(Juegos.class, id);  // Busca la entidad por su id
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }

        return Optional.ofNullable(juego);  // Devuelve un Optional con el resultado
    }

    @Override
    public List<Juegos> findAll() {
        // Abre una sesión de Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Juegos> juegos = null;

        try {
            // Ejecuta una consulta HQL para obtener todos los juegos
            juegos = session.createQuery("FROM Juegos", Juegos.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();  // Cierra la sesión
        }

        return juegos;  // Devuelve la lista de juegos
    }
}
