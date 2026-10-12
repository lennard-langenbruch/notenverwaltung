package de.fh.swf.notenverwaltung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.fh.swf.notenverwaltung.entity.Abschlussarbeit;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.AbschlussarbeitRepository;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class AbschlussarbeitServiceTest {

	@Mock
	StudentRepository studentRepository;

	@Mock
	AbschlussarbeitRepository abschlussarbeitRepository;

	@InjectMocks
	AbschlussarbeitService abschlussarbeitService;

	private Student student = new Student("lennard", "langenbruch");

	@Test
	void createAbschlussarbeit_ohneVorhandeneArbeit_wirdAngelegtUndVerknuepft() {
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);
		when(abschlussarbeitRepository.findOneByThema("Thesis")).thenReturn(null);

		abschlussarbeitService.createAbschlussarbeit("Thesis", "Beschreibung", "5.0", "2.7");

		ArgumentCaptor<Abschlussarbeit> captor = ArgumentCaptor.forClass(Abschlussarbeit.class);
		verify(abschlussarbeitRepository).save(captor.capture());
		Abschlussarbeit a = captor.getValue();
		assertEquals("Thesis", a.getThema());
		assertEquals("Beschreibung", a.getBeschreibung());
		assertTrue(a.getBestanden());
		assertEquals("2.7", a.getEndnote());
		assertSame(student, a.getStudent());
		assertSame(a, student.getAbschlussarbeit());
	}

	@Test
	void createAbschlussarbeit_studentHatBereitsArbeit_speichertNichts() {
		student.setAbschlussarbeit(new Abschlussarbeit("Altes Thema"));
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		abschlussarbeitService.createAbschlussarbeit("Thesis", "Beschreibung", "1.0", "");

		verify(abschlussarbeitRepository, never()).save(any());
	}

	@Test
	void deleteAbschlussarbeit_loeschtUndEntferntVerknuepfung() {
		Student s = mock(Student.class);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(s);

		abschlussarbeitService.deleteAbschlussarbeit(5L);

		verify(abschlussarbeitRepository).deleteById(5L);
		verify(s).setAbschlussarbeit(null);
		verify(s).berechnen();
	}

	@Test
	void findAll_liefertAlleArbeiten() {
		List<Abschlussarbeit> arbeiten = List.of(new Abschlussarbeit("Thesis"));
		when(abschlussarbeitRepository.findAll()).thenReturn(arbeiten);

		assertEquals(arbeiten, abschlussarbeitService.findAll());
	}

}
