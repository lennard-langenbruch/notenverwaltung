package de.fh.swf.notenverwaltung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.fh.swf.notenverwaltung.entity.Pflichtfach;
import de.fh.swf.notenverwaltung.entity.Student;
import de.fh.swf.notenverwaltung.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	StudentRepository studentRepository;

	@InjectMocks
	StudentService studentService;

	@BeforeEach
	void setUp() {
		Pflichtfach mathe = new Pflichtfach("Mathe", "5", "1");
		mathe.setEndnote("2.0");
		Pflichtfach programmierung = new Pflichtfach("Programmierung", "10", "1");
		programmierung.setEndnote("1.0");
		Pflichtfach offen = new Pflichtfach("Physik", "5", "2");

		Student student = new Student("lennard", "langenbruch");
		student.setPflicht(List.of(mathe, programmierung, offen));
		when(studentRepository.findOneByNachname("langenbruch")).thenReturn(student);
	}

	@Test
	void getNotendurchschnitt_gewichtetNachCreditpoints() {
		// (2.0 * 5 + 1.0 * 10) / 15, Fächer ohne Endnote zählen nicht
		assertEquals(String.valueOf(20.0f / 15.0f), studentService.getNotendurchschnitt());
	}

	@Test
	void getSummeCreditpoints_zaehltNurFaecherMitEndnote() {
		assertEquals("15", studentService.getSummeCreditpoints());
	}

}
