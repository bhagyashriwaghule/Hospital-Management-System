package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.ResultSet;

public class HospitalManagementSystem {
	//private static final String url  ="jdbc:mysql://localhost:3306/hosptial";
	//private static final String username  ="rooot";
	//private static final String password  ="root";
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scanner=new Scanner(System.in);
		try {
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hosptial","root","root");
			Patient patient=new Patient(connection,scanner);
			Doctor doctor=new Doctor(connection);
			while(true)
			{
				System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patient");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5.  Exit");
				System.out.println("Enter your choice: ");
				int choice=scanner.nextInt();
				switch(choice)
				{
				case 1:
					//add patient
					patient.addPatient();
					System.out.println();
					break;

				case 2:
					//view patient
					patient.viewPatients();
					System.out.println();
					break;
				case 3:
					//view doctors
					doctor.viewDoctors();
					System.out.println();
					break;
				case 4:
					//book appoinment
					bookAppointment(patient, doctor, connection, scanner);
					System.out.println();
					break;
				case 5:
					System.out.println("THANK YOU HOSPITAL MANAEMENT SYSTEM!!");
					return;
				default:
					System.out.println("enter valid choice!!!");
					break;
				}
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}


	public static void bookAppointment(Patient patient,Doctor doctor, Connection connection,Scanner scanner)
	{
		System.out.println("Enter Patient Id: ");
		int patientId=scanner.nextInt();
		System.out.println("Enter Doctor Id: ");
		int doctorId=scanner.nextInt();
		System.out.println("Enter appointment date(YYYY-MM-DD): ");
		String appointmentDate=scanner.next();
		if(patient.getPatientById(patientId)&&doctor.getDoctorById(doctorId)) {

			if(checkdoctorAvailability(doctorId,appointmentDate, connection)) {
				String appointmentQuery="insert into appointments(patient_id,doctor_id,appointment_date)values(?,?,?)";
				try {
					PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, patientId);
					preparedStatement.setInt(2, doctorId);
					preparedStatement.setString(3,appointmentDate);
					int rowsAffected=preparedStatement.executeUpdate();
					if(rowsAffected>0) {
						System.out.println("Appoinment Booked");
					}else
					{
						System.out.println("failed to Book Appointment");
					}
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("Doctor not available on this date!!");
			}


		}
		else{
			{
				System.out.println("Either doctor or patient doesnt exist!!!");

			}

		}	}
	public static  boolean checkdoctorAvailability(int doctorId,String appointmentDate,Connection connection)
	{
		String query="select count(*)from appointments where doctor_id =? and appointment_date =?";
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString(2,appointmentDate);
			java.sql.ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next())
			{
				int count=resultSet.getInt(1);
				if(count==0)
				{
					return true;
				}else {
					return false;
				}
			}



		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;

	}
}




