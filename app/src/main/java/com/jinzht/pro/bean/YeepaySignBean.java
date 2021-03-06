package com.jinzht.pro.bean;

/**
 * 易宝签名
 */
public class YeepaySignBean {

    /**
     * message : 加密成功!
     * status : 200
     * data : {"sign":"MIIFKQYJKoZIhvcNAQcCoIIFGjCCBRYCAQExCzAJBgUrDgMCGgUAMC8GCSqGSIb3DQEHAaAiBCBhZWRmODhmNWI0YWZhMmU5ZGY5ZDg1Y2IzMzUzMmRjNKCCA+swggPnMIIDUKADAgECAhBqe6nrCt+fci3xZjQH7hHsMA0GCSqGSIb3DQEBBQUAMCoxCzAJBgNVBAYTAkNOMRswGQYDVQQKExJDRkNBIE9wZXJhdGlvbiBDQTIwHhcNMTAxMjA2MDQ0ODE1WhcNMTExMjA2MDQ0ODE1WjCBgTELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMjEWMBQGA1UECxMNcmEueWVlcGF5LmNvbTESMBAGA1UECxMJQ3VzdG9tZXJzMSkwJwYDVQQDFCAwNDFAWjEwMDAwMzY1NjQyQGxpYmluZ0AwMDAwMDAwMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAuDInQe7L04o8f0pVUrAF52yEWFZiVpqzZXr7BpFll0qVaRNHVwycO2z/kpWMYGy2YXi2I16Wtn+9SVwjcVOqUBXQMGvuiDApJ7mJNN0VkLNppRCpJpAZWRbSh2Sn7zxn768dsu2Et+saIOGpSYCjBJBlmn7KS7t84E4tU3EFCPkCAwEAAaOCAbQwggGwMB8GA1UdIwQYMBaAFPCN7bNBu/vvCB5VAsMxN+88FE7NMB0GA1UdDgQWBBRWmAkcFN/mHizNprNca0Rdge/6YDALBgNVHQ8EBAMCBeAwDAYDVR0TBAUwAwEBADAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwgf0GA1UdHwSB9TCB8jBWoFSgUqRQME4xCzAJBgNVBAYTAkNOMRswGQYDVQQKExJDRkNBIE9wZXJhdGlvbiBDQTIxDDAKBgNVBAsTA0NSTDEUMBIGA1UEAxMLY3JsMTAxXzcwNDAwgZeggZSggZGGgY5sZGFwOi8vY2VydDg2My5jZmNhLmNvbS5jbjozODkvQ049Y3JsMTAxXzcwNDAsT1U9Q1JMLE89Q0ZDQSBPcGVyYXRpb24gQ0EyLEM9Q04/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MDQGAypWAQQtEytDVVNUT01FUl9UWFA7MTAwMDAzNjU2NDI7aGsxMDAxMDAxQHRlc3QuY29tMA0GCSqGSIb3DQEBBQUAA4GBAAEe1lD6k0PCl8vTwOTGupqfTVhPtjlZyycWZmYkdlQhFlyFMd7TLH7d7BGbCPjqlyTq58dCy+OT9+ZSZm+fq/PnRF+Wu5yKmAup2dakSiXq/HJlPgYg2X2DmtkfAcg34LTmbNeJafpwfAwqHWrlu1E8vBzZs0b7oH8Xca5yoQEcMYHjMIHgAgEBMD4wKjELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMgIQanup6wrfn3It8WY0B+4R7DAJBgUrDgMCGgUAMA0GCSqGSIb3DQEBAQUABIGAsRqsAcP/rMegoptwcwIRyhsDBvB5I/IYtew8PvJPuHhkzBCj3kSEUVLFAIGQc5NGzVK6oC+W9GtKg3EWiBFcYe8XmH2Xma1OSFtM6f6T7WxHO5gvZa9wWNRpoZLcv/37VcGh4YkWF1/wr0ckQTf6vc1DhsOxhLM1w0kiPSEl9Fc="}
     */

    private String message;
    private int status;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sign : MIIFKQYJKoZIhvcNAQcCoIIFGjCCBRYCAQExCzAJBgUrDgMCGgUAMC8GCSqGSIb3DQEHAaAiBCBhZWRmODhmNWI0YWZhMmU5ZGY5ZDg1Y2IzMzUzMmRjNKCCA+swggPnMIIDUKADAgECAhBqe6nrCt+fci3xZjQH7hHsMA0GCSqGSIb3DQEBBQUAMCoxCzAJBgNVBAYTAkNOMRswGQYDVQQKExJDRkNBIE9wZXJhdGlvbiBDQTIwHhcNMTAxMjA2MDQ0ODE1WhcNMTExMjA2MDQ0ODE1WjCBgTELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMjEWMBQGA1UECxMNcmEueWVlcGF5LmNvbTESMBAGA1UECxMJQ3VzdG9tZXJzMSkwJwYDVQQDFCAwNDFAWjEwMDAwMzY1NjQyQGxpYmluZ0AwMDAwMDAwMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAuDInQe7L04o8f0pVUrAF52yEWFZiVpqzZXr7BpFll0qVaRNHVwycO2z/kpWMYGy2YXi2I16Wtn+9SVwjcVOqUBXQMGvuiDApJ7mJNN0VkLNppRCpJpAZWRbSh2Sn7zxn768dsu2Et+saIOGpSYCjBJBlmn7KS7t84E4tU3EFCPkCAwEAAaOCAbQwggGwMB8GA1UdIwQYMBaAFPCN7bNBu/vvCB5VAsMxN+88FE7NMB0GA1UdDgQWBBRWmAkcFN/mHizNprNca0Rdge/6YDALBgNVHQ8EBAMCBeAwDAYDVR0TBAUwAwEBADAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwgf0GA1UdHwSB9TCB8jBWoFSgUqRQME4xCzAJBgNVBAYTAkNOMRswGQYDVQQKExJDRkNBIE9wZXJhdGlvbiBDQTIxDDAKBgNVBAsTA0NSTDEUMBIGA1UEAxMLY3JsMTAxXzcwNDAwgZeggZSggZGGgY5sZGFwOi8vY2VydDg2My5jZmNhLmNvbS5jbjozODkvQ049Y3JsMTAxXzcwNDAsT1U9Q1JMLE89Q0ZDQSBPcGVyYXRpb24gQ0EyLEM9Q04/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MDQGAypWAQQtEytDVVNUT01FUl9UWFA7MTAwMDAzNjU2NDI7aGsxMDAxMDAxQHRlc3QuY29tMA0GCSqGSIb3DQEBBQUAA4GBAAEe1lD6k0PCl8vTwOTGupqfTVhPtjlZyycWZmYkdlQhFlyFMd7TLH7d7BGbCPjqlyTq58dCy+OT9+ZSZm+fq/PnRF+Wu5yKmAup2dakSiXq/HJlPgYg2X2DmtkfAcg34LTmbNeJafpwfAwqHWrlu1E8vBzZs0b7oH8Xca5yoQEcMYHjMIHgAgEBMD4wKjELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMgIQanup6wrfn3It8WY0B+4R7DAJBgUrDgMCGgUAMA0GCSqGSIb3DQEBAQUABIGAsRqsAcP/rMegoptwcwIRyhsDBvB5I/IYtew8PvJPuHhkzBCj3kSEUVLFAIGQc5NGzVK6oC+W9GtKg3EWiBFcYe8XmH2Xma1OSFtM6f6T7WxHO5gvZa9wWNRpoZLcv/37VcGh4YkWF1/wr0ckQTf6vc1DhsOxhLM1w0kiPSEl9Fc=
         */
        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
