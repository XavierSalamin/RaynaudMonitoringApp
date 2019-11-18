package fr.hes.raynaudmonitoring.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


public class FileDownloadUtils {

	/** The Constant LOGGER. */


	/** The Constant HEADER_VALUE_ATTACHEMENT. */
	private static final String HEADER_VALUE_ATTACHEMENT = "attachment;filename=";

	/**
	 * Instantiates a new file download utils.
	 */
	private FileDownloadUtils() {
		// static final class private constructor
	}

	/**
	 * Download as streaming response body.
	 *
	 * @param workbook
	 *            the workbook
	 * @param fileName
	 *            the file name
	 * @param download
	 *            the download
	 * @return the response entity
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ResponseEntity<StreamingResponseBody> downloadAsStreamingResponseBody(final Workbook workbook,
			final String fileName, final boolean download) throws IOException {

		// Get input stream from workbook
		final byte[] outArray = workbookToByteArray(workbook);
		final ByteArrayInputStream bis = new ByteArrayInputStream(outArray);

		return downloadAsStreamingResponseBody(bis, fileName, null, download);
	}

	/**
	 * Download an {@link InputStream} as a {@link StreamingResponseBody}.
	 *
	 * @param inputStream
	 *            the input stream to download
	 * @param fileName
	 *            the file name given to the downloaded file
	 * @param download
	 *            indicates that the header "Content-Disposition: attachment;filename=..." must be added
	 * @return the streaming response body for controller download
	 */
	public static ResponseEntity<StreamingResponseBody> downloadAsStreamingResponseBody(final InputStream inputStream,
			final String fileName, final boolean download) {

		return downloadAsStreamingResponseBody(inputStream, fileName, null, download);
	}

	/**
	 * Download an {@link InputStream} as a {@link StreamingResponseBody}.
	 *
	 * @param inputStream
	 *            the input stream to download
	 * @param fileName
	 *            the file name given to the downloaded file
	 * @param contentType
	 *            the content type
	 * @param download
	 *            indicates that the header "Content-Disposition: attachment;filename=..." must be added
	 * @return the streaming response body for controller download
	 */
	public static ResponseEntity<StreamingResponseBody> downloadAsStreamingResponseBody(final InputStream inputStream,
			final String fileName, final String contentType, final boolean download) {
		// The streaming response body
		StreamingResponseBody body;
		// The http headers
		HttpHeaders responseHeaders;
		try {

			final byte[] bytes = IOUtils.toByteArray(inputStream);

			// Create the response body
			body = getStreamingResponseBody(bytes);

			// Prepare the http headers

			responseHeaders = prepareHttpHeaders(bytes, fileName, contentType, download);


		} catch (final IOException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Write workbook to byte array.
	 *
	 * @param workbook
	 *            the workbook
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] workbookToByteArray(final Workbook workbook) throws IOException {
		// Write to byte array output stream
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		// Close the workbook
		workbook.close();

		// Get the byte array
		final byte[] outArray = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();

		return outArray;
	}



	/**
	 * Get the streaming response body from an {@link InputStream}.
	 *
	 * @param bytes
	 *            the bytes
	 * @return the streaming response body
	 */
	private static StreamingResponseBody getStreamingResponseBody(final byte[] bytes) {
		return (final OutputStream outputStream) -> {
			try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
				// Copy the input stream to output stream
				IOUtils.copy(bis, outputStream);
			}
		};
	}



	/**
	 * Get the http headers for a byte array with content type parameter.
	 *
	 * @param bytes
	 *            the bytes
	 * @param fileName
	 *            the file name
	 * @param contentType
	 *            the content type
	 * @param download
	 *            the download
	 * @return the http headers
	 */
	private static HttpHeaders prepareHttpHeaders(final byte[] bytes, final String fileName, final String contentType,
			final boolean download) {

		return prepareHttpHeaders(bytes.length, fileName, contentType, download);
	}

	/**
	 * Get the http headers.
	 *
	 * @param contentLength
	 *            the content length
	 * @param fileName
	 *            the file name
	 * @param contentType
	 *            the content type
	 * @param download
	 *            the download
	 * @return the http headers
	 */
	private static HttpHeaders prepareHttpHeaders(final long contentLength, final String fileName,
			final String contentType,
			final boolean download) {
		final HttpHeaders responseHeaders = new HttpHeaders();

		responseHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);
		responseHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");
		responseHeaders.setContentLength(contentLength);

		if (download) {
			responseHeaders
			.add(HttpHeaders.CONTENT_DISPOSITION, HEADER_VALUE_ATTACHEMENT + "\"" + fileName + "\"");
		}

		return responseHeaders;
	}

}
