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

import de.fh.swf.notenverwaltung.entity.Pflichtfach;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.FachRepository;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class PflichtfachServiceTest {

	@Mock
	StudentRepository studentRepository;

	@Mock
	FachRepository fachRepository;

	@InjectMocks
	PflichtfachService pflichtfachService;

	private Student student = new Student("lennard", "langenbruch");

	private Pflichtfach createAndCapture(String note1, String note2, String note3) {
		when(fachRepository.findOneByFachname("Mathe")).thenReturn(null);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		pflichtfachService.createFach("1", "5", "Mathe", note1, note2, note3);

		ArgumentCaptor<Pflichtfach> captor = ArgumentCaptor.forClass(Pflichtfach.class);
		verify(fachRepository).save(captor.capture());
		return captor.getValue();
	}

	@Test
	void createFach_mitBestandenerNote_vergibtCreditpoints() {
		Pflichtfach fach = createAndCapture("2.3", "", "");

		assertEquals("Mathe", fach.getFachname());
		assertTrue(fach.getBestanden());
		assertEquals("2.3", fach.getEndnote());
		assertTrue(fach.getCreditpointsGranted());
		assertSame(student, fach.getStudent_pflicht());
		assertEquals(5, student.getSummeCreditpoints());
	}

	@Test
	void createFach_mitUngueltigerNote_verwirftNote() {
		Pflichtfach fach = createAndCapture("6.0", "", "");

		assertEquals("", fach.getNote1());
		assertFalse(fach.getBestanden());
		assertEquals("", fach.getEndnote());
		assertEquals(0, student.getSummeCreditpoints());
	}

	@Test
	void createFach_zweiterVersuchNachNichtBestanden_zaehlt() {
		Pflichtfach fach = createAndCapture("5.0", "3.0", "");

		assertEquals("5.0", fach.getNote1());
		assertEquals("3.0", fach.getNote2());
		assertTrue(fach.getBestanden());
		assertEquals("3.0", fach.getEndnote());
	}

	@Test
	void createFach_zweiterVersuchOhneNichtBestanden_wirdVerworfen() {
		Pflichtfach fach = createAndCapture("2.0", "3.0", "");

		assertEquals("", fach.getNote2());
		assertEquals("2.0", fach.getEndnote());
	}

	@Test
	void createFach_dritterVersuchNachZweiMalNichtBestanden_zaehlt() {
		Pflichtfach fach = createAndCapture("5.0", "5.0", "4.0");

		assertEquals("4.0", fach.getNote3());
		assertTrue(fach.getBestanden());
		assertEquals("4.0", fach.getEndnote());
	}

	@Test
	void createFach_vorhandenesFach_wirdAktualisiert() {
		Pflichtfach vorhanden = new Pflichtfach("Mathe", "5", "1");
		when(fachRepository.findOneByFachname("Mathe")).thenReturn(vorhanden);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);

		pflichtfachService.createFach("2", "10", "Mathe", "1.7", "", "");

		verify(fachRepository).save(vorhanden);
		assertEquals("2", vorhanden.getSemester());
		assertEquals("10", vorhanden.getCreditpoints());
		assertEquals("1.7", vorhanden.getEndnote());
		assertTrue(vorhanden.getBestanden());
	}

	@Test
	void deleteFach_loeschtUndBerechnetNeu() {
		Student s = mock(Student.class);
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(s);

		pflichtfachService.deleteFach(42L);

		verify(fachRepository).deleteById(42L);
		verify(s).berechnen();
	}

	@Test
	void findAll_liefertAlleFaecher() {
		List<Pflichtfach> faecher = List.of(new Pflichtfach("Mathe", "5", "1"));
		when(fachRepository.findAll()).thenReturn(faecher);

		assertEquals(faecher, pflichtfachService.findAll());
	}

	@Test
	void findByFachname_liefertListeMitFach() {
		Pflichtfach fach = new Pflichtfach("Mathe", "5", "1");
		when(fachRepository.findOneByFachname("Mathe")).thenReturn(fach);

		assertEquals(List.of(fach), pflichtfachService.findByFachname("Mathe"));
	}

}
