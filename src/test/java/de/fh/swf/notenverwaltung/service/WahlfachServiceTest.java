package de.fh.swf.notenverwaltung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.entity.Wahlfach;
import de.fh.swf.notenverwaltung.repository.StudentRepository;
import de.fh.swf.notenverwaltung.repository.WahlfachRepository;

@ExtendWith(MockitoExtension.class)
class WahlfachServiceTest {

	@Mock
	StudentRepository studentRepository;

	@Mock
	WahlfachRepository wahlfachRepository;

	@InjectMocks
	WahlfachService wahlfachService;

	private Student student = new Student("lennard", "langenbruch");

	@Test
	void createWahlfach_neuesFach_wirdGespeichert() {
		when(wahlfachRepository.findOneByFachname("Skriptsprachen")).thenReturn(null);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		wahlfachService.createWahlfach("Skriptsprachen", "1.3", "", "");

		ArgumentCaptor<Wahlfach> captor = ArgumentCaptor.forClass(Wahlfach.class);
		verify(wahlfachRepository).save(captor.capture());
		Wahlfach fach = captor.getValue();
		assertEquals("Skriptsprachen", fach.getFachname());
		assertTrue(fach.getBestanden());
		assertEquals("1.3", fach.getEndnote());
		assertSame(student, fach.getStudent_wahl());
	}

	@Test
	void createWahlfach_vorhandenesFach_wirdAktualisiert() {
		Wahlfach vorhanden = new Wahlfach("Skriptsprachen");
		when(wahlfachRepository.findOneByFachname("Skriptsprachen")).thenReturn(vorhanden);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		wahlfachService.createWahlfach("Skriptsprachen", "5.0", "", "");

		verify(wahlfachRepository).save(vorhanden);
		assertEquals("5.0", vorhanden.getNote1());
		assertFalse(vorhanden.getBestanden());
		assertEquals("", vorhanden.getEndnote());
	}

	@Test
	void deleteWahlfach_loeschtUndBerechnetNeu() {
		Student s = mock(Student.class);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(s);

		wahlfachService.deleteWahlfach(7L);

		verify(wahlfachRepository).deleteById(7L);
		verify(s).berechnen();
	}

	@Test
	void findAll_liefertAlleWahlfaecher() {
		List<Wahlfach> faecher = List.of(new Wahlfach("Datenbanken 3"));
		when(wahlfachRepository.findAll()).thenReturn(faecher);

		assertEquals(faecher, wahlfachService.findAll());
	}

	@Test
	void findByFachname_liefertListeMitFach() {
		Wahlfach fach = new Wahlfach("Datenbanken 3");
		when(wahlfachRepository.findOneByFachname("Datenbanken 3")).thenReturn(fach);

		assertEquals(List.of(fach), wahlfachService.findByFachname("Datenbanken 3"));
	}

}
