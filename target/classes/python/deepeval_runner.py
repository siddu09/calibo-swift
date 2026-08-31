import json
import sys

from deepeval.metrics import AnswerRelevancyMetric
from deepeval.test_case import LLMTestCase

question = sys.argv[1]
answer = sys.argv[2]
context = sys.argv[3]
metric_name = sys.argv[4]

test_case = LLMTestCase(
    input=question,
    actual_output=answer,
    retrieval_context=[context]
)

metric = AnswerRelevancyMetric(
    threshold=0.5
)

metric.measure(test_case)

result = {
    "score": round(metric.score, 2),
    "passed": metric.success,
    "reason": metric.reason,
    "metric": metric_name
}

print(json.dumps(result))
