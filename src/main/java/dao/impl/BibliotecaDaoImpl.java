package dao.impl;

import dao.BibliotecaDao;
import models.Biblioteca;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class BibliotecaDaoImpl implements BibliotecaDao {

    @Override
    public void save(Biblioteca entity) {
        // Abre una sesión de Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inicia una transacción
            Transaction transaction = session.beginTransaction();

            try {
                session.persist(entity);  // Guarda la entidad en la base de datos (equivalente a 'save')
                transaction.commit();  // Confirma la transacción
            } catch (Exception e) {
                transaction.rollback();  // Revertir la transacción en caso de error
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Biblioteca entity) {
        // Abre una sesión de Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inicia una transacción
            Transaction transaction = session.beginTransaction();

            try {
                session.merge(entity);  // Actualiza la entidad en la base de datos (equivalente a 'update')
                transaction.commit();  // Confirma la transacción
            } catch (Exception e) {
                transaction.rollback();  // Revertir la transacción en caso de error
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        // Abre una sesión de Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inicia una transacción
            Transaction transaction = session.beginTransaction();

            try {
                Biblioteca biblioteca = session.get(Biblioteca.class, id);  // Busca la entidad por su id
                if (biblioteca != null) {
                    session.remove(biblioteca);  // Elimina la entidad de la base de datos (equivalente a 'delete')
                    transaction.commit();  // Confirma la transacción
                }
            } catch (Exception e) {
                transaction.rollback();  // Revertir la transacción en caso de error
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Biblioteca> findById(String id) {
        // Abre una sesión de Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Biblioteca biblioteca = session.get(Biblioteca.class, id);  // Busca la entidad por su id
            return Optional.ofNullable(biblioteca);  // Devuelve un Optional con el resultado
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();  // En caso de error, devuelve Optional vacío
        }
    }

    @Override
    public List<Biblioteca> findAll() {
        // Abre una sesión de Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Ejecuta una consulta HQL para obtener todas las bibliotecas
            Query<Biblioteca> query = session.createQuery("FROM Biblioteca", Biblioteca.class);
            return query.list();  // Devuelve la lista de bibliotecas
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // En caso de error, devuelve null
        }
    }
}
