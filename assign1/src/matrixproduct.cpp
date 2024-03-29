#include <stdio.h>
#include <iostream>
#include <iomanip>
#include <time.h>
#include <cstdlib>
#include <papi.h>
#include <math.h>
#include <string.h>
#include <fstream>

using namespace std;

#define SYSTEMTIME clock_t

 
void OnMult(int m_ar, int m_br,ofstream& file) 
{
	
	SYSTEMTIME Time1, Time2;
	
	char st[100];
	double temp;
	int i, j, k;

	double *pha, *phb, *phc;
	

		
    pha = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phb = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phc = (double *)malloc((m_ar * m_ar) * sizeof(double));

	for(i=0; i<m_ar; i++)
		for(j=0; j<m_ar; j++)
			pha[i*m_ar + j] = (double)1.0;



	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phb[i*m_br + j] = (double)(i+1);

	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phc[i*m_br + j] = (double)(i+1);




    Time1 = clock();

	for(i=0; i<m_ar; i++)
	{	for( j=0; j<m_br; j++)
		{	temp = 0;
			for( k=0; k<m_ar; k++)
			{	
				temp += pha[i*m_ar+k] * phb[k*m_br+j];
			}
			phc[i*m_ar+j]=temp;
		}
	}


    Time2 = clock();
	sprintf(st, "Time: %3.3f seconds\n", (double)(Time2 - Time1) / CLOCKS_PER_SEC);
	cout << st;
	file << st;
	file << "Flops : " << (2 * (m_ar * m_ar * m_ar)) / (double)(Time2 - Time1) / CLOCKS_PER_SEC << endl;
	// display 10 elements of the result matrix tto verify correctness
	cout << "Result matrix: " << endl;
	for(i=0; i<1; i++)
	{	for(j=0; j<min(10,m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

    free(pha);
    free(phb);
    free(phc);
	
	
}

// add code here for line x line matriz multiplication
void OnMultLine(int m_ar, int m_br, ofstream& file)
{
	SYSTEMTIME Time1, Time2;
	
	char st[100];
	double temp;
	int i, j, k;

	double *pha, *phb, *phc;


	pha = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phb = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phc = (double *)malloc((m_ar * m_ar) * sizeof(double));

	for(i=0; i<m_ar; i++)
		for(j=0; j<m_ar; j++)
			pha[i*m_ar + j] = (double)1.0;



	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phb[i*m_br + j] = (double)(i+1);

	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phc[i*m_br + j] = (double)(i+1);


	Time1 = clock();

	for(int j=0;j<m_br;j++){
		for(int i=0;i<m_ar;i++){
			for(int k = 0; k < m_ar; k++){
				phc[j*m_ar + k] += pha[j*m_ar + i] * phb[i*m_br + k];
			}
		}
	}
	Time2 = clock();
	sprintf(st, "Time: %3.3f seconds\n", (double)(Time2 - Time1) / CLOCKS_PER_SEC);
	cout << st;
	file << st;
	file << "Flops : " << (2 * (m_ar * m_ar * m_ar)) / (double)(Time2 - Time1) / CLOCKS_PER_SEC << endl;
	// display 10 elements of the result matrix tto verify correctness
	cout << "Result matrix: " << endl;
	for(i=0; i<1; i++)
	{	for(j=0; j<min(10,m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

    free(pha);
    free(phb);
    free(phc);
}

// add code here for block x block matriz multiplication
void OnMultBlock(int m_ar, int m_br, int bkSize,ofstream& file)
{
	SYSTEMTIME Time1, Time2;
	
	char st[100];
	double temp;
	int i, j, k;

	double *pha, *phb, *phc;


	pha = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phb = (double *)malloc((m_ar * m_ar) * sizeof(double));
	phc = (double *)malloc((m_ar * m_ar) * sizeof(double));

	for(i=0; i<m_ar; i++)
		for(j=0; j<m_ar; j++)
			pha[i*m_ar + j] = (double)1.0;
	
	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phb[i*m_br + j] = (double)(i+1);
	

	for(i=0; i<m_br; i++)
		for(j=0; j<m_br; j++)
			phc[i*m_br + j] = (double)0.0;


	Time1 = clock();
	
	for(int ii = 0; ii < m_ar; ii += bkSize){
		for (int jj = 0; jj < m_br; jj += bkSize){
			for(int kk = 0; kk < m_br; kk += bkSize){
				for(int i = ii; i < ii + bkSize; i++){
						for(int j = jj; j < jj + bkSize; j++){
							for(int k = kk; k < kk + bkSize; k++){
								phc[i * m_ar + k] += pha[i * m_ar + j] * phb[j * m_br + k];
							}
						}
					}
				}
			}
		}





	Time2 = clock();
	sprintf(st, "Time: %3.3f seconds\n", (double)(Time2 - Time1) / CLOCKS_PER_SEC);
	long long size = m_ar * m_ar * m_ar 	;
	cout << "Flops : " << (2 * (m_ar * m_ar * m_ar)) / (double)(Time2 - Time1) / CLOCKS_PER_SEC << endl;
	
	cout << st;
	file << st;
	file << "Flops : " << (2 * (m_ar * m_ar * m_ar)) / (double)(Time2 - Time1) / CLOCKS_PER_SEC << endl;
	// display 10 elements of the result matrix tto verify correctness
	cout << "Result matrix: " << endl;
	for(i=0; i<1; i++)
	{	for(j=0; j<min(10,m_br); j++)
			cout << phc[j] << " ";
	}
	cout << endl;

    free(pha);
    free(phb);
    free(phc);


}



void handle_error (int retval)
{
  printf("PAPI error %d: %s\n", retval, PAPI_strerror(retval));
  exit(1);
}

void init_papi() {
  int retval = PAPI_library_init(PAPI_VER_CURRENT);
  if (retval != PAPI_VER_CURRENT && retval < 0) {
    printf("PAPI library version mismatch!\n");
    exit(1);
  }
  if (retval < 0) handle_error(retval);

  std::cout << "PAPI Version Number: MAJOR: " << PAPI_VERSION_MAJOR(retval)
            << " MINOR: " << PAPI_VERSION_MINOR(retval)
            << " REVISION: " << PAPI_VERSION_REVISION(retval) << "\n";
}


int main (int argc, char *argv[])
{
	char c;
	int lin, col, blockSize;
	int op;
	
	int EventSet = PAPI_NULL;
  	long long values[2];
  	int ret;
	

	ret = PAPI_library_init( PAPI_VER_CURRENT );
	if ( ret != PAPI_VER_CURRENT )
		std::cout << "FAIL" << endl;


	ret = PAPI_create_eventset(&EventSet);
		if (ret != PAPI_OK) cout << "ERROR: create eventset" << endl;


	ret = PAPI_add_event(EventSet,PAPI_L1_DCM );
	if (ret != PAPI_OK) cout << "ERROR: PAPI_L1_DCM" << endl;


	ret = PAPI_add_event(EventSet,PAPI_L2_DCM);
	if (ret != PAPI_OK) cout << "ERROR: PAPI_L2_DCM" << endl;


	ret = PAPI_add_event(EventSet,PAPI_LD_INS);
	if (ret != PAPI_OK) cout << "ERROR: PAPI_LD_INS" << endl;


	ret = PAPI_add_event(EventSet,PAPI_SR_INS);
	if (ret != PAPI_OK) cout << "ERROR: PAPI_SR_INS" << endl;

	ret = PAPI_add_event(EventSet,PAPI_PRF_DM);
	if (ret != PAPI_OK) cout << "ERROR: PAPI_PRF_DM" << endl;
	

	op=1;
	do {
		cout << endl << "1. Multiplication" << endl;
		cout << "2. Line Multiplication" << endl;
		cout << "3. Block Multiplication" << endl;
		cout << "Selection?: ";
		cin >>op;
		if (op == 0)
			break;
		printf("Dimensions: lins=cols ? ");
   		cin >> lin;
   		col = lin;
		ofstream myFile;
		string filename = to_string(op) + " - " + to_string(lin) + "x" + to_string(lin);
			// Start counting
		ret = PAPI_start(EventSet);
		if (ret != PAPI_OK) cout << "ERROR: Start PAPI" << endl;

		switch (op){
			case 1:
				myFile.open(filename);
				OnMult(lin, col,myFile);
				break;
			case 2:
				myFile.open(filename);
				OnMultLine(lin, col,myFile);  
				break;
			case 3:
				cout << "Block Size? ";
				cin >> blockSize;
				string newFilename = to_string(op) + " - " + to_string(lin) + "x" + to_string(lin) + " bkSize: " + to_string(blockSize);
				myFile.open(newFilename);
				OnMultBlock(lin, col, blockSize,myFile);  
				break;

		}

  		ret = PAPI_stop(EventSet, values);
  		if (ret != PAPI_OK) cout << "ERROR: Stop PAPI" << endl;
  		printf("L1 DCM: %lld \n",values[0]);
  		printf("L2 DCM: %lld \n",values[1]);
		myFile << "L1 DCM: " << values[0] << endl;
  		myFile << "L2 DCM: " << values[1] << endl;

		double sum = values[2] + values[3];


		cout << "L1 data cache hit rate: " << 1.0 - (values[0] / sum) << endl;
		myFile << "L1 data cache hit rate: " << 1.0 - (values[0] / sum) << endl;


		ret = PAPI_reset( EventSet );
		if ( ret != PAPI_OK )
			std::cout << "FAIL reset" << endl; 



	}while (op != 0);

	ret = PAPI_remove_event( EventSet, PAPI_L1_DCM );
	if ( ret != PAPI_OK )
		std::cout << "FAIL remove event" << endl; 

	ret = PAPI_remove_event( EventSet, PAPI_L2_DCM );
	if ( ret != PAPI_OK )
		std::cout << "FAIL remove event" << endl; 

	ret = PAPI_destroy_eventset( &EventSet );
	if ( ret != PAPI_OK )
		std::cout << "FAIL destroy" << endl;

}