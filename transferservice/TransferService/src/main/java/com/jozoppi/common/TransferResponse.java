package com.jozoppi.common;
/**
 * @author  Giorgio Zoppi
 * @email <giorgio.zoppi@gmail.com>
 *  Customizing the response to success queries in case fo transfer.
 */

    public final class TransferResponse extends BaseResponse
    {
        private static final String SUCCESS_MESSAGE = "Transfer Created Successfully";
        private String sourceId;
        private String destinationId;
        private double amount;

        public TransferResponse() {
            super(SUCCESS_MESSAGE);
        }

        public TransferResponse(String sourceId, String destinationId, double amount)
        {
            super(SUCCESS_MESSAGE);
            this.destinationId = destinationId;
            this.sourceId = sourceId;
            this.amount = amount;
        }

            public String getSourceId()
            {
                return  this.sourceId;
            }
            public  void setSourceId(String sourceId)
            {
                this.sourceId = sourceId;
            }
            public  void setAmount(double amount)
            {
                this.amount = amount;
            }
            public double getAmount()
            {
                return  this.amount;
            }
            public  void setDestinationId(String destinationId)
            {
                this.destinationId = destinationId;
            }
            public String getDestinationId()
            {
                return this.destinationId;
            }
    }
